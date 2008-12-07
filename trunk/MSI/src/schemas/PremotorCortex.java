package schemas;
import java.util.*;
import schema_output.*;
import java.util.concurrent.*;

/**
 * The premotor cortex computes movement signals based
 * on the forward model input. In mental state inference,
 * the movement signals do not actually move the organism's
 * actuators. Instead, they serve as feedback to the forward
 * models to continue simulating.
 * @author Prateek Tandon
 *
 */
public class PremotorCortex extends BrainSchema {
	
	/**
	 * Received inputs from the prefrontal cortex. Based on the 
	 * task parameter of the prefrontal cortex, the premotor cortex
	 * can be on or off in mental state inference mode. If this was
	 * a robot, the switch could go back into actor mode.
	 */
	private Queue<PrefrontalCortexOutput> pfcInput;
	
	/**
	 * Received inputs from the parietal cortex.
	 */
	private Queue<ParietalCortexOutput> pcInput;

	/**
	 * Received inputs from the forward models.
	 */
	private Queue<ControlVariableSummary> fmInput;

	/**
	 * The current task parameter.
	 */
	private double currentTaskParameter = 0.0;

	/**
	 * Connection to forward models
	 */
	private List<ForwardModel> forwardModels;
	
	/**
	 * Constructor
	 */
	public PremotorCortex() {
		super("Premotor Cortex");
		pfcInput = new ConcurrentLinkedQueue<PrefrontalCortexOutput>();
		pcInput = new ConcurrentLinkedQueue<ParietalCortexOutput>();
		fmInput = new ConcurrentLinkedQueue<ControlVariableSummary>();
	}
	
	/**
	 * Used by parietal cortex to send input to this schema.
	 * @param input The input from parietal cortex
	 */
	public void sendPCInput(ParietalCortexOutput input) {
		this.printDebug("Received parietal input");
		pcInput.add(input);
		receivedInput();
	}
	
	/**
	 * Used by Prefrontal cortex to send input to this schema
	 * @param input The input from the prefrontal cortex
	 */
	public void sendPFCInput(PrefrontalCortexOutput input) {
		pfcInput.add(input);
		receivedInput();
	}
	
	/**
	 * Used by forward models to send input to this schema
	 */
	public void sendFMInput(ControlVariableSummary input) {
		fmInput.add(input);
		receivedInput();
	}
	
	/**
	 * Helper method to compute movement signal.
	 * @return Movement signal
	 */
	private String computeMovementSignal() {
		if(currentTaskParameter < 0) {
			return "";
		}
		else {
			return "straight";
		}
	}
	
	/**
	 * Handles necessary inputs to produce
	 * necessary outputs.
	 */
	public boolean produceOutput() {
		
		//reset case
		if(resetSignals.size() > 0) {
			resetSignals.clear();
			pfcInput.clear();
			pcInput.clear();
			fmInput.clear();
			currentTaskParameter = 0.0;
			return false;
		}

		//receive task parameter from pfc if pfc sends something.
		if(pfcInput.size() > 0) {
			currentTaskParameter = pfcInput.remove().getTaskParameter();
			return false;
		}
		
		//only do  stuff if task parameter says to
		if(currentTaskParameter > 0) {
			if(pcInput.size() > 0) {
				ParietalCortexOutput pcIn = pcInput.remove();
				this.printDebug("Sent init signal to ALL forward models");
				PremotorCortexOutput output_initSignal = new PremotorCortexOutput(pcIn.getDistanceFromCenter(), pcIn.getOrientationAngleDifference());
				output_initSignal.setMotorCommand(null);
				
				//send init signal to all forward models
				for(ForwardModel fm : forwardModels) {
					fm.sendPCInput(output_initSignal);
				}
				
				return true;
			}
		
			if(fmInput.size() > 0) {
				ControlVariableSummary fmIn = fmInput.remove();
//				this.printDebug("Sent movement signal to " + fmIn.getSender().getName() + " " + fmIn.getTimeStep());
				PremotorCortexOutput output_movementSignal = new PremotorCortexOutput(this.computeMovementSignal());

				//send movement signal to forward model that asked for it
				fmIn.getSender().sendPCInput(output_movementSignal);
			
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Sets up connection to forward models
	 * @param forwardModels connections
	 */
	public void setForwardModels(List<ForwardModel> forwardModels) {
		this.forwardModels = forwardModels;
	}
}
