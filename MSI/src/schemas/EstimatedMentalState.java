package schemas;
import java.util.*;
import java.util.concurrent.*;
import schema_output.*;
import gui.*;
import game.*;

/**
 * The estimated mental state schema takes in the discounted difference
 * measures for each hypothesized mental state from the difference module.
 * It then computes the probability of each hypothesis, outputing
 * the inferred mental state.
 * @author Prateek Tandon
 *
 */
public class EstimatedMentalState extends BrainSchema {
			
	/**
	 * Map of forward models names to current discounted differences
	 */
	private Map<String, Double> fmToDD;
	
	/**
	 * Received records from difference module
	 */
	private Queue<DifferenceModuleOutput> dmInput;
	
	/**
	 * Received inputs from story master 
	 * (for game prototype)
	 */
	private Queue<GameOutput> gameInput;
	
	/**
	 * Connection to gui schema
	 */
	private GUISchema gm;
	
	/**
	 * Constructor
	 */
	public EstimatedMentalState() {
		super("Estimated Mental State");		
		fmToDD = Collections.synchronizedMap(new HashMap<String, Double>());
		dmInput = new ConcurrentLinkedQueue<DifferenceModuleOutput>();
		gameInput = new ConcurrentLinkedQueue<GameOutput>();
	}
	
	/**
	 * Used by difference module to send input to this schema.
	 * @param input The input to send
	 */
	public void sendDMOutput(DifferenceModuleOutput input) {
		dmInput.add(input);
		receivedInput();
	}
	
	/**
	 * Used by story master schema in game prototype to communicate
	 * with this schema
	 */
	public void sendGameInput(GameOutput gm) {
		gameInput.add(gm);
		receivedInput();
	}
	
	/**
	 * Produces necessary outputs based on received inputs.
	 */
	public boolean produceOutput() {
		
		//reset case
		if(resetSignals.size() > 0) {
			resetSignals.clear();
			fmToDD.clear();
			dmInput.clear();
			return false;
		}
		
		if(dmInput.size() > 0) {
			DifferenceModuleOutput dmIn = dmInput.remove();
			
			//update buffers
			for(FMDDPair p : dmIn.getPairs()) {
				fmToDD.put(p.getForwardModel(), p.getDiscountedDifference());
			}
			
			//identify most likely mental state
			double min = Double.MAX_VALUE;
			String winner = null;
			for(String f : fmToDD.keySet()) {
				double dd = fmToDD.get(f);
				if(dd < min) {
					min=dd;
					winner = f;
				}
			}
			
			//compute total sum
			double totalSum = 0.0;
			for(FMDDPair p : dmIn.getPairs()) {
				totalSum += Math.pow(Math.E, -0.02*p.getDiscountedDifference());
			}
			
			//compute probs
			Map<String, Double> fmToProb =  new HashMap<String, Double>();
			for(FMDDPair p : dmIn.getPairs()) {
				double prob =  computeProbability(totalSum, p.getDiscountedDifference());
				fmToProb.put(p.getForwardModel(),prob);
			}
						
			//send to prefrontal cortex and gui!
			
			//send to gui
			EstimatedMentalStateOutput output1 = new EstimatedMentalStateOutput(winner, fmToProb, dmIn.getTimeStep());
			this.printDebug("WINNER: " + output1.toString());
			gm.sendEMInput(output1);
			
			//send to story manager if
			//reached end of simulation
			if(gameInput.size() > 0) {
				GameOutput gm = gameInput.peek();
				StoryMaster sm = gm.getSm();
				
				String msiAction = gm.getMsiAction();
				if(msiAction.equalsIgnoreCase("Nailing") && output1.getTimeStep() >=35) {
					EstimatedMentalStateOutput output2 = new EstimatedMentalStateOutput(winner, fmToProb, dmIn.getTimeStep());
					sm.sendEMInput(output2);
					gameInput.remove();
				}
				if(msiAction.equalsIgnoreCase("Prying") && output1.getTimeStep() >= 30) {
					EstimatedMentalStateOutput output2 = new EstimatedMentalStateOutput(winner, fmToProb, dmIn.getTimeStep());
					sm.sendEMInput(output2);
					gameInput.remove();
				}
				if(msiAction.equalsIgnoreCase("Holding") && output1.getTimeStep() >= 20) {
					EstimatedMentalStateOutput output2 = new EstimatedMentalStateOutput(winner, fmToProb, dmIn.getTimeStep());
					sm.sendEMInput(output2);
					gameInput.remove();
				}
			}
			
			return true;			
		}
		return false;
	}
	
	/**
	 * Used to compute the probability of a mental state, given
	 * the discounted difference for a given forward model.
	 * @param totalSum Sum of normalized discounted difference
	 * @param dd The discounted difference
	 * @return
	 */
	private double computeProbability(double totalSum, double dd) {
		double mine = Math.pow(Math.E, -0.02*dd);
		return mine / totalSum * 100;
	}
	
	/**
	 * Sets connection to gui schema
	 * @param gm Gui Schema
	 */
	public void setGUISchema(GUISchema gm) {
		this.gm = gm;
	}
}
