package schemas;
import java.util.*;
import java.util.concurrent.*;
import schema_output.*;

public class EstimatedMentalState extends BrainSchema {
	
	/*
	 * List of Forward Models
	 */
	private List<ForwardModel> fmList;
	
	/**
	 * Map of forward models to current discounted differences
	 */
	private Map<ForwardModel, Double> fmToDD;
	
	/**
	 * Received records from difference module
	 */
	private Queue<DifferenceModuleOutput> dmInput;
	
	public EstimatedMentalState() {
		super("Estimated Mental State");
		
		fmList = Collections.synchronizedList(new ArrayList<ForwardModel>());
		fmToDD = Collections.synchronizedMap(new HashMap<ForwardModel, Double>());
		dmInput = new ConcurrentLinkedQueue<DifferenceModuleOutput>();
	}

	public void sendDMOutput(DifferenceModuleOutput input) {
		dmInput.add(input);
		receivedInput();
	}
	
	public boolean produceOutput() {
		if(dmInput.size() > 0) {
			DifferenceModuleOutput dmIn = dmInput.remove();
			
			//update buffers
			for(FMDDPair p : dmIn.getPairs()) {
				fmToDD.put(p.getForwardModel(), p.getDiscountedDifference());
			}
			
			//identify most likely mental state
			double min = Double.MAX_VALUE;
			ForwardModel winner = null;
			for(ForwardModel f : fmToDD.keySet()) {
				double dd = fmToDD.get(f);
				if(dd < min) {
					min=dd;
					winner = f;
				}
			}
			
			//compute probs
			Map<ForwardModel, Double> fmToProb =  new HashMap<ForwardModel, Double>();
			for(FMDDPair p : dmIn.getPairs()) {
				fmToProb.put(p.getForwardModel(), computeProbability(dmIn, p.getDiscountedDifference()));
			}
			
			EstimatedMentalStateOutput output = new EstimatedMentalStateOutput(winner, fmToProb);
			
			//send to prefrontal cortex and gui!
			this.printDebug("WINNER: " + output.toString());
			
			return true;			
		}
		return false;
	}
	
	public double computeProbability(DifferenceModuleOutput dmIn, double dd) {
		return 0.0;
	}
}
