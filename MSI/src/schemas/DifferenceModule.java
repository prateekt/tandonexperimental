package schemas;
import java.util.*;
import java.util.concurrent.*;
import schema_output.*;
import util.*;
import util.*;
import gui.*;

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
	
	public void sendFMInput(ControlVariableSummary input) {
//		this.printDebug("Received Forward Model info from " + input.getSender().getName());
		int index = fmToIndex.get(input.getSender());
		Queue<ControlVariableSummary> queue = predCVs.get(index);
		queue.add(input);
//		receivedInput();
	}
	
	public void sendPCInput(ParietalCortexOutput input) {
//		this.printDebug("Received parietal input of real cvs");
		obsCVs.add(input);
		pcInputDisposable.add(input);
		receivedInput();
	}
	
	public double computeDiscountedDifference(List<ControlVariableSummary> simCVs, List<ParietalCortexOutput> realCVs) {
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
	
	private List<ControlVariableSummary> queueToList_cv(Queue<ControlVariableSummary> queue) {
		List<ControlVariableSummary> rtn = new ArrayList<ControlVariableSummary>();
		rtn.addAll(queue);
		return rtn;
	}
	
	private List<ParietalCortexOutput> queueToList_pc(Queue<ParietalCortexOutput> queue) {
		List<ParietalCortexOutput> rtn = new ArrayList<ParietalCortexOutput>();
		rtn.addAll(queue);
		return rtn;
	}
	
	public boolean produceOutput() {
				
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
				
				List<FMDDPair> rtn = new ArrayList<FMDDPair>();
				for(Queue<ControlVariableSummary> simCVs : predCVs) {
					double dd = computeDiscountedDifference(queueToList_cv(simCVs), queueToList_pc(obsCVs));
					FMDDPair toAdd = new FMDDPair(simCVs.peek().getSender(), dd);
					rtn.add(toAdd);
//					this.printDebug("DEF");
	//				this.printDebug(toAdd.getForwardModel().getName() + "::" + toAdd.getDiscountedDifference());
	//				this.printDebug("ENDDEF");
				}
				
				DifferenceModuleOutput output = new DifferenceModuleOutput(rtn);
				
				//send to Estimated Mental State
				estimatedMentalState.sendDMOutput(output);			

				//send current control variables to GUI
				for(Queue<ControlVariableSummary> simCVs : predCVs) {
					if(simCVs.peek().getSender()==Constants.getForwardModels().get(1)) {
						List<ControlVariableSummary> list = queueToList_cv(simCVs);
						gm.sendFMInput(list.get(obsCVs.size()-1));
						break;
					}
				}
				
				return true;
			}
		}
		
		return false;
	}
	
	public void setForwardModels(List<ForwardModel> fmList) {
		this.fmList = fmList;
	}
	
	public void setEstimatedMentalState(EstimatedMentalState estimatedMentalState) {
		this.estimatedMentalState = estimatedMentalState;
	}
	
	public void setGuiSchema(GUISchema gm) {
		this.gm = gm;
	}
}
