package schemas;
import schema_output.*;
import java.util.*;
import java.util.concurrent.*;

/**
 * The prefrontal cortex sends task parameters to different brain 
 * schemas to control switching. In my implementation it serves
 * as the 'on' and 'reset' signal for the brain model.
 * @author Prateek Tandon
 *
 */
public class PrefrontalCortex extends BrainSchema {

	/**
	 * The received inputs from estimated mental state schema.
	 */
	private Queue<EstimatedMentalStateOutput> inputList;

	/**
	 * The current task parameter
	 */
	private int taskParameterCounter = 0;

	/**
	 * The connection to the premotor cortex
	 */
	private PremotorCortex premotorCortex;
	
	/**
	 * The connection to the parietal cortex
	 */
	private ParietalCortex parietalCortex;
	
	/**
	 * The constructor
	 */
	public PrefrontalCortex() {
		super("Prefrontal Cortex");
		inputList = new ConcurrentLinkedQueue<EstimatedMentalStateOutput>();
	}
	
	/**
	 * Used by estimated mental state schema to send
	 * input to the prefrontal cortex.
	 * @param input
	 */
	public void sendInput(EstimatedMentalStateOutput input) {
//		this.printDebug("Received inferrred mental state");
		inputList.add(input);
		receivedInput();
	}
	
	/**
	 * Helper method to generate output
	 * @param input The input from the inferred mental state
	 * @return My output
	 */
	private PrefrontalCortexOutput getOutput(EstimatedMentalStateOutput input) {
		return new PrefrontalCortexOutput(++taskParameterCounter);
	}
	
	/**
	 * Handles necessary inputs to produce necessary outputs.
	 */
	public boolean produceOutput() {
		//handle reset case
		if(resetSignals.size() > 0) {
			resetSignals.clear();
			inputList.clear();
			return false;
		}
		
		if(inputList.size() > 0) {
			PrefrontalCortexOutput output = getOutput(inputList.remove());
			this.printDebug("Sent task parameter");
			
			//send output to premotor cortex and parietal cortex
			premotorCortex.sendPFCInput(output);
			parietalCortex.sendTaskParameter(output);
			
			return true;
		}
		return false;
	}
	
	/**
	 * Sets up connection to premotor cortex
	 * @param premotorCortex premotor cortex
	 */
	public void setPremotorCortex(PremotorCortex premotorCortex) {
		this.premotorCortex =premotorCortex;
	}
	
	/**
	 * Sets up connection to parietal cortex
	 * @param parietalCortex parietal cortex
	 */
	public void setParietalCortex(ParietalCortex parietalCortex) {
		this.parietalCortex = parietalCortex;
	}
}
