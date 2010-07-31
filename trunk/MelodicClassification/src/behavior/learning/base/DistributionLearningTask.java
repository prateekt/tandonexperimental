package behavior.learning.base;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class DistributionLearningTask {
	
	protected List<String> valueObservations;
	protected Map<String, Double> histoBelief;
	
	public DistributionLearningTask(String[] possibleValues) {
		valueObservations = new ArrayList<String>();
		
		//instantiate belief -- no bias
		histoBelief = new LinkedHashMap<String, Double>();

		/*
		if(possibleValues!=null) {
			for(String value : possibleValues) {
				histoBelief.put(value, 0.0);
			}
		}*/
	}
	
	public String getMLE() {
		double max = Double.NEGATIVE_INFINITY;
		String rtn = null;
		for(String key : histoBelief.keySet()) {
			double prob = histoBelief.get(key);
			if(prob >= max) {
				max = prob;
				rtn = key;
			}
		}

		return rtn;
	}
	
	private void updateBelief() {			

		//recompute all beliefs
		histoBelief = new LinkedHashMap<String, Double>();
		int numObs = valueObservations.size();
		for(String obs : valueObservations) {
			
			//update histogram
			if(!histoBelief.containsKey(obs)) {
				histoBelief.put(obs, 1.0/numObs);
			}
			else {
				histoBelief.put(obs,histoBelief.get(obs) + 1.0/numObs);
			}
		}
	}
	
	public Map<String, Double> getBelief(String obs) {

		//easy exit
		if(obs==null)
			return histoBelief;
		
		//else recompute based on observation
		valueObservations.add(obs);
		updateBelief();
		return histoBelief;
	}	
}
