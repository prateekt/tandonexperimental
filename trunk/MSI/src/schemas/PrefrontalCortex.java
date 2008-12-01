package schemas;
import schema_output.*;
import java.util.*;
import java.util.concurrent.*;

public class PrefrontalCortex extends BrainSchema {

	private Queue<EstimatedMentalStateOutput> inputList;
	private int taskParameterCounter = 0;
	private PremotorCortex premotorCortex;
	private ParietalCortex parietalCortex;
	
	public PrefrontalCortex() {
		super("Prefrontal Cortex");
		inputList = new ConcurrentLinkedQueue<EstimatedMentalStateOutput>();
	}
	
	public void sendInput(EstimatedMentalStateOutput input) {
		this.printDebug("Received inferrred mental state");
		inputList.add(input);
		receivedInput();
	}
	
	public PrefrontalCortexOutput getOutput(EstimatedMentalStateOutput input) {
		return new PrefrontalCortexOutput(++taskParameterCounter);
	}
	
	public boolean produceOutput() {
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
	
	public void setPremotorCortex(PremotorCortex premotorCortex) {
		this.premotorCortex =premotorCortex;
	}
	
	public void setParietalCortex(ParietalCortex parietalCortex) {
		this.parietalCortex = parietalCortex;
	}
}
