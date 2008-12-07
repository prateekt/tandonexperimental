package schemas;
import java.util.*;
import java.util.concurrent.*;
import schema_output.*;
import util.*;
import util.*;
import gui.*;

/**
 * The difference module compares the history of observed control
 * variables and simulated control variables and determines the discounted
 * difference for each hypothesized mental state. Relays this
 * information to the estimated mental state schema which will compute
 * the probabilities of the belief states.
 * @author Prateek Tandon
 *
 */
public class DifferenceModule extends BrainSchema {
	
	/**
	 * List of connected forward  models.
	 */
	private List<ForwardModel> fmList;
	
	/**
	 * Mapping of forward model to index in list.
	 */
	private Map<ForwardModel, Integer> fmToIndex;
	
	/**
	 * List of queues of predicted control variables for forward models.
	 */
	private List<Queue<ControlVariableSummary>> predCVs;
	
	/**
	 * Queue of observed control variables
	 */
	private Queue<ParietalCortexOutput> obsCVs;
	
	/**
	 * Queue of removable control variables
	 */
	private Queue<ParietalCortexOutput> pcInputDisposable;
	
	/**
	 * Connection to estimated mental state schema
	 */
	private EstimatedMentalState estimatedMentalState;
	
	/**
	 * Connection to gui
	 */
	private GUISchema gm;
	
	/**
	 * Constructor
	 */
	public DifferenceModule() {
		super("Difference Module");
		fmList = Collections.synchronizedList(new ArrayList<ForwardModel>());
		fmToIndex = Collections.synchronizedMap(new HashMap<ForwardModel, Integer>());

		//declare add fm to list
		for(ForwardModel fm : Constants.getForwardModels()) {
			fmList.add(fm);
		}
		
		predCVs = Collections.synchronizedList(new ArrayList<Queue<ControlVariableSummary>>());
		
		int index=0;
		for(ForwardModel f: fmList) {
			predCVs.add(new ConcurrentLinkedQueue<ControlVariableSummary>());
			fmToIndex.put(f, index);
			index++;
		}
		
		obsCVs = new ConcurrentLinkedQueue<ParietalCortexOutput>();
		pcInputDisposable = new ConcurrentLinkedQueue<ParietalCortexOutput>();
	}
	
	/**
	 * Used by forward model to send input to dm.
	 * @param input
	 */
	public void sendFMInput(ControlVariableSummary input) {
//		this.printDebug("Received Forward Model info from " + input.getSender().getName());
		int index = fmToIndex.get(input.getSender());
		Queue<ControlVariableSummary> queue = predCVs.get(index);
		queue.add(input);
//		receivedInput();
	}
	
	/**
	 * Used by parietal cortex to send input to dm.
	 * @param input
	 */
	public void sendPCInput(ParietalCortexOutput input) {
//		this.printDebug("Received parietal input of real cvs");
		obsCVs.add(input);
		pcInputDisposable.add(input);
		receivedInput();
	}
	
	/**
	 * Helper method to compute discounted difference
	 * @param simCVs The list of simulated control variables
	 * @param realCVs The list of real control variables
	 * @return Discounted difference
	 */
	private double computeDiscountedDifference(List<ControlVariableSummary> simCVs, List<ParietalCortexOutput> realCVs) {
		int n = realCVs.size();
		double a = 0.9;
		
		double coeff = (1 - a) / (1 - Math.pow(a, n+1));
		
		double sumTerm = 0.0;
		
		for(int i=0; i < n; i++) {	
	        double[][] cvarsArr = {{simCVs.get(i).getDistanceFromCenter() - realCVs.get(i).getDistanceFromCenter()}, {simCVs.get(i).getOrientationDifference() - realCVs.get(i).getOrientationAngleDifference()}};
	        double[][] WArr = {{0.7, 0}, {0, 0.3}};
	        Matrix cVars = new Matrix(cvarsArr);
	        Matrix cVars_transpose = cVars.transpose();
	        Matrix W = new Matrix(WArr);
	        Matrix intermediate = cVars_transpose.times(W);
	        Matrix result = intermediate.times(cVars); 
	        double a_sumTerm = Math.pow(a, n - i);
	        sumTerm += result.get(0,0) * a_sumTerm;
		}
		
		return coeff * sumTerm; 		
	}
	
	/**
	 * Converts control variables queue to list.
	 */
	private List<ControlVariableSummary> queueToList_cv(Queue<ControlVariableSummary> queue, int size) {
		List<ControlVariableSummary> total = new ArrayList<ControlVariableSummary>();
		total.addAll(queue);
		
		List<ControlVariableSummary> rtn = new ArrayList<ControlVariableSummary>();
		for(int x=0; x < size; x++) {
			rtn.add(total.get(x));
		}
	
		return rtn;
	}
	
	/**
	 * Converts parietal cortex output queue to list.
	 */
	private List<ParietalCortexOutput> queueToList_pc(Queue<ParietalCortexOutput> queue) {
		List<ParietalCortexOutput> rtn = new ArrayList<ParietalCortexOutput>();
		rtn.addAll(queue);
		return rtn;
	}
	
	/**
	 * Helper method that generates output that is own object
	 * in clean manner.
	 * Avoids concurrent modification exceptions.
	 * @return difference module output
	 */
	private DifferenceModuleOutput getOutput() {
		List<FMDDPair> rtn = new ArrayList<FMDDPair>();
		
		//these are the queues that contain summaries for each
		//forward model
		for(Queue<ControlVariableSummary> simCVs : predCVs) {
			double dd = computeDiscountedDifference(queueToList_cv(simCVs, obsCVs.size()), queueToList_pc(obsCVs));
			FMDDPair toAdd = new FMDDPair(simCVs.peek().getSender().getName(), dd);
			rtn.add(toAdd);
		}

		DifferenceModuleOutput output = new DifferenceModuleOutput(rtn, obsCVs.size());
		return output;
	}
	
	/**
	 * Produces necessary outputs based on inputs.
	 */
	public boolean produceOutput() {
		
		//handle reset case
		if(resetSignals.size() > 0) {
			resetSignals.clear();
			for(Queue<ControlVariableSummary > q : predCVs) {
				q.clear();
			}
			obsCVs.clear();
			pcInputDisposable.clear();
			return false;
		}
		
		//do callback on getting new observations
		//timescales of obscv and predcv are different w/ predcv
		//much faster. Callback on obscv.
		if(pcInputDisposable.size() > 0) {
			
			//get minimum in predicted value q
			//used to determine whether can do dd computation.
			int minInPred = Integer.MAX_VALUE;
			for(Queue<ControlVariableSummary> q : predCVs) {
				if(q.size() < minInPred)
					minInPred = q.size();
			}
			
			if(obsCVs.size() > 0 && minInPred >= obsCVs.size()) {
//				this.printDebug("Computing discounted differences: ");
				ParietalCortexOutput pcIn = pcInputDisposable.remove();
								
				//send to Estimated Mental State
				DifferenceModuleOutput output1 = this.getOutput();
				estimatedMentalState.sendDMOutput(output1);
				
				
				//send dd's to gui
				DifferenceModuleOutput output2 = this.getOutput();
				gm.sendDMInput(output2);

				//get simulated control variables
				Map<String, Double> simDOC = new HashMap<String, Double>();
				Map<String, Double> simOA = new HashMap<String, Double>();
				int timeStep =obsCVs.size();
				for(Queue<ControlVariableSummary> queue : predCVs) {
					List<ControlVariableSummary> listRep = queueToList_cv(queue, obsCVs.size());
					ControlVariableSummary last = listRep.get(listRep.size()-1);
					simDOC.put(last.getSender().getName(), last.getDistanceFromCenter());
					simOA.put(last.getSender().getName(),last.getOrientationDifference());
				}
				
				//get observed control variables
				List<ParietalCortexOutput> listRep = queueToList_pc(obsCVs);
				ParietalCortexOutput current = listRep.get(listRep.size()-1);
				double realDOC = current.getDistanceFromCenter();
				double realOA = current.getOrientationAngleDifference();
				
				//send observed and simulated control variables to GUI
				gm.sendDMCurrentVars(realDOC, realOA, simDOC, simOA, timeStep);
				
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Sets connection to the forward models in the simulation
	 * @param fmList The forward models
	 */
	public void setForwardModels(List<ForwardModel> fmList) {
		this.fmList = fmList;
	}
	
	/**
	 * Sets connection to the estimated mental state schema
	 * @param estimatedMentalState The estimated mental state schema
	 */
	public void setEstimatedMentalState(EstimatedMentalState estimatedMentalState) {
		this.estimatedMentalState = estimatedMentalState;
	}
	
	/**
	 * Sets connection to the gui schema
	 * @param gm  The gui schema
	 */
	public void setGuiSchema(GUISchema gm) {
		this.gm = gm;
	}
}
