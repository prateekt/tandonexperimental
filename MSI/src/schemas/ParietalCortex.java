package schemas;

import schema_output.*;
import util.Vector2;
import java.util.*;
import java.util.concurrent.*;

public class ParietalCortex extends BrainSchema {
	
	/**
	 * List of received inputs from visual cortex
	 */
	private Queue<VisualCortexOutput> vcInput;
	
	/**
	 * List of received inputs from prefrontal cortex
	 */
	private Queue<PrefrontalCortexOutput> pfcInput;
		
	/**
	 * Task parameter as received from pfc
	 */
	private int taskParameter = -1;
	
	/**
	 * Stores whether sent initial input to premotor cortex
	 */
	private boolean sentInitToPC = false;
	
	/**
	 * Connection to premotor cortex
	 */
	private PremotorCortex pc;
	
	/**
	 * Connection to difference module
	 */
	private DifferenceModule dm;
	
	public ParietalCortex() {
		super("Parietal Cortex");
		vcInput = new ConcurrentLinkedQueue<VisualCortexOutput>();
		pfcInput = new ConcurrentLinkedQueue<PrefrontalCortexOutput>();
	}
	
	/**
	 * Method called by visual cortex to send input to parietal
	 * cortex
	 * @param input The output of the vc
	 */
	public void sendVisualInput(VisualCortexOutput input) {
		vcInput.add(input);
		receivedInput();
	}
	
	/**
	 * Method called by prefrontal cortex to send task parameter
	 * data to parietal cortex.
	 * 
	 * @param input The output of the pfc
	 */
	public void sendTaskParameter(PrefrontalCortexOutput input) {
		pfcInput.add(input);
		receivedInput();
	}
	
	public boolean produceOutput() {
		
		//change task parameter based on
		//what pfc says.
		if(pfcInput.size() > 0) {
			PrefrontalCortexOutput output = pfcInput.remove();
			taskParameter = output.getTaskParameter();
			sentInitToPC = false;
		}

		//only process vc output if 
		//have task for doing so.
		if(taskParameter > 0) {
			if(vcInput.size() > 0) {
				ParietalCortexOutput output = getOutput(vcInput.remove());
				if(output==null) {
					this.printDebug("FALSE OUTPUT");
					output = ParietalCortexOutput.randForDebug();
				}
				
				if(!sentInitToPC) {
					//send to premotor cortex
					pc.sendPCInput(output);
					sentInitToPC = true;
				}
				
				//always send to difference module!
				dm.sendPCInput(output);
				
				return true;
			}
		}

		return false;
	}

	public ParietalCortexOutput getOutput(VisualCortexOutput input) {

		//hammer handle vector
		LargestBlob green = input.getGreenTag();
		LargestBlob blue = input.getBlueTag();
		
		//knuckle vector
		LargestBlob pink = input.getPinkTag();
		LargestBlob yellow = input.getYellowTag();

		if(green==null || blue==null || pink==null || yellow==null) {
			return null;
		}
		
		//compute hammer handle vector
		Vector2 dirVector = new Vector2(blue.getCX() - green.getCX(), blue.getCY() - green.getCY());
		Vector2 hammerHandleVector = dirVector.scale(0.5);
		double centerOfHammerX =  green.getCX() + hammerHandleVector.getX();
		double centerOfHammerY =  green.getCY() + hammerHandleVector.getY();
		hammerHandleVector.setVectorStartX(centerOfHammerX);
		hammerHandleVector.setVectorStartY(centerOfHammerY);
		
		//compute knuckle vector
		Vector2 knuckleVector = new Vector2(pink.getCX() - yellow.getCX(), pink.getCY() - yellow.getCY());
		knuckleVector.setVectorStartX(yellow.getCX());
		knuckleVector.setVectorStartY(yellow.getCY());
		Vector2 toCenterOfHand = knuckleVector.scale(0.5);
		double centerOfHandX = yellow.getCX() + toCenterOfHand.getX();
		double centerOfHandY = yellow.getCY() + toCenterOfHand.getY();
		
		//compute orientation difference
		double costheta = hammerHandleVector.dot(knuckleVector) / (hammerHandleVector.getLength() * knuckleVector.getLength());
		double theta = Math.acos(costheta);
		
		//compute distance from center
		double doc = Math.sqrt(Math.pow(centerOfHammerX - centerOfHandX, 2) + Math.pow(centerOfHammerY - centerOfHandY, 2));
		
		//return
		ParietalCortexOutput rtn = new ParietalCortexOutput(doc, theta);
		rtn.setHammerHandleVector(hammerHandleVector);
		rtn.setKnuckleVector(knuckleVector);
		return rtn;
	}	
	
	public void setPremotorCortex(PremotorCortex pc) {
		this.pc = pc;
	}
	
	public void setDifferenceModule(DifferenceModule dm) {
		this.dm = dm;
	}
}