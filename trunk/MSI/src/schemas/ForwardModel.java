package schemas;
import java.util.*;
import schema_output.*;
import java.util.concurrent.*;
import util.*;
import gui.*;

public abstract class ForwardModel extends BrainSchema {
	
	/**
	 * The number of time steps to mentally simulate for.
	 * This is necessary to determine how long we think
	 * the actor will need to reach a stable hand state. 
	 */
	protected int numTimeSteps;
	
	/**
	 * The current time step of mental simulation.
	 */
	protected int currentTimeStep;
	
	/**
	 * The final expected knuckle vector and hammer handle vector
	 * angular orientation difference.
	 */
	protected double finalOrientationDifference;
	
	/**
	 * The final expected distance from center of the hand from the hammer
	 * for this forward model.
	 */
	protected double finalDistanceFromCenter;
		
	/**
	 * Queue of forward model inputs from last time step.
	 */
	private Queue<ControlVariableSummary> fmInput;
	
	/**
	 * Queue of motor commands from premotor cortex
	 */
	private Queue<PremotorCortexOutput> pcInput;
	
	/**
	 * Connection to premotor cortex
	 */
	private PremotorCortex pc;
	
	/**
	 * Connection to difference module
	 */
	private DifferenceModule dm;
		
	/**
	 * Constructor
	 * @param name The name of the forward model (specific to
	 * forward model type)s
	 */
	public ForwardModel(String name) {
		super(name);
		fmInput = new ConcurrentLinkedQueue<ControlVariableSummary>();
		pcInput = new ConcurrentLinkedQueue<PremotorCortexOutput>();
		numTimeSteps = Constants.NUMBER_TIME_STEPS;
	}
	
	public void sendFMInput(ControlVariableSummary input) {
		fmInput.add(input);
		receivedInput();
	}
	
	public void sendPCInput(PremotorCortexOutput input) {
		pcInput.add(input);
		receivedInput();
	}
	
	public ControlVariableSummary getOutput(ControlVariableSummary cv, String motorCommand) {
		if(motorCommand.equalsIgnoreCase("straight")) {
			//use straight line prediction
			double oldDoc = cv.getDistanceFromCenter();
			double oldOA =  cv.getOrientationDifference();
			
			double distanceToGo = finalDistanceFromCenter - oldDoc;
			double oaToGo = finalOrientationDifference -  oldOA;
			
			double timeToGo = numTimeSteps - currentTimeStep;
			
			double docRate = distanceToGo / timeToGo;
			double oaRate = oaToGo / timeToGo;
			
			double newDoc = oldDoc +  docRate;
			double newOA = oldOA  + oaRate;
			
			return new ControlVariableSummary(this, newDoc, newOA, currentTimeStep++);
		}
		
		return null; //error case
	}
		
	public boolean produceOutput() {
		
		//do stuff only if pc has sent stuff
		if(pcInput.size() > 0) {
			
			if(pcInput.peek().getMotorCommand()==null) {
				//if it is a init command, reset and init variables.

				//reset
				currentTimeStep = 0;
				fmInput.clear();
				PremotorCortexOutput pcIn = pcInput.remove();
				pcInput.clear();
				
				//init variables
				double doc = pcIn.getDistanceFromCenterInit();
				double oa = pcIn.getOrientationAngleDifferenceInit();
				ControlVariableSummary output = new ControlVariableSummary(this, doc, oa, currentTimeStep++);

				//send output to premotor cortex and difference module
				//and self
				pc.sendFMInput(output);
				sendFMInput(output);
				dm.sendFMInput(output);

				return true;
			}
			else {
				//if it is a motor command, update forward model
				if(fmInput.size() > 0 && currentTimeStep < numTimeSteps) {					
					ControlVariableSummary cvIn = fmInput.remove();
					PremotorCortexOutput pcIn = pcInput.remove();
					String motorCommand = pcIn.getMotorCommand();
					ControlVariableSummary output =  getOutput(cvIn, motorCommand);
//					this.printDebug("Sent out new control vars: " + output.getDistanceFromCenter() + " " + output.getOrientationDifference());
					
					//send output to premotor cortex and difference module
					//and self
					pc.sendFMInput(output);
					dm.sendFMInput(output);
					sendFMInput(output);
					
					return true;
				}
			}
		}		
		return false;
	}
	
	public void setPremotorCortex(PremotorCortex pc) {
		this.pc = pc;
	}
	
	public void setDifferenceModule(DifferenceModule dm) {
		this.dm = dm;
	}
	
	/**
	 * @return the finalOrientationDifference
	 */
	public double getFinalOrientationDifference() {
		return finalOrientationDifference;
	}

	/**
	 * @return the finalDistanceFromCenter
	 */
	public double getFinalDistanceFromCenter() {
		return finalDistanceFromCenter;
	}
}
