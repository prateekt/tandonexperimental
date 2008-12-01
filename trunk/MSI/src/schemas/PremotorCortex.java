package schemas;
import java.util.*;
import schema_output.*;
import java.util.concurrent.*;

public class PremotorCortex extends BrainSchema {
	
	private Queue<PrefrontalCortexOutput> pfcInput;
	private Queue<ParietalCortexOutput> pcInput;
	private Queue<ControlVariableSummary> fmInput;
	private double currentTaskParameter = 0.0;
	private List<ForwardModel> forwardModels;
	
	public PremotorCortex() {
		super("Premotor Cortex");
		pfcInput = new ConcurrentLinkedQueue<PrefrontalCortexOutput>();
		pcInput = new ConcurrentLinkedQueue<ParietalCortexOutput>();
		fmInput = new ConcurrentLinkedQueue<ControlVariableSummary>();
	}
	
	public void sendPCInput(ParietalCortexOutput input) {
		this.printDebug("Received parietal input");
		pcInput.add(input);
		receivedInput();
	}
	
	public void sendPFCInput(PrefrontalCortexOutput input) {
		pfcInput.add(input);
		receivedInput();
	}
	
	public void sendFMInput(ControlVariableSummary input) {
		fmInput.add(input);
		receivedInput();
	}
	
	public String computeMovementSignal() {
		if(currentTaskParameter < 0) {
			return "";
		}
		else {
			return "straight";
		}
	}
	
	public boolean produceOutput() {
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
	
	public void setForwardModels(List<ForwardModel> forwardModels) {
		this.forwardModels = forwardModels;
	}
}
