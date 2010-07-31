package behavior.learning.base;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class DistributionSetLearningTask {

	protected List<Map<String, Double>> valueObservations;
	protected Map<String, Double> histoBelief;

	public DistributionSetLearningTask(String[] possibleValues) {
		valueObservations = new ArrayList<Map<String, Double>>();
		
		//instantiate belief -- no bias
		histoBelief = new LinkedHashMap<String, Double>();
		for(String value : possibleValues) {
			histoBelief.put(value, 0.0);
		}
	}	
	
	private void updateBelief() {
		if(valueObservations.size()==1) {
			
			//put original beleif
			Map<String, Double> currentObs = valueObservations.get(0);
			for(String key : currentObs.keySet()) {
				histoBelief.put(key, currentObs.get(key));
			}
			
		}
		else {
			
			//recompute all beliefs
			int numObs = valueObservations.size();
			for(String obs : histoBelief.keySet()) {
				
				double sum = 0.0;
				for(Map<String,Double> histo : valueObservations) {
					double toAdd=0.0;
					if(histo.containsKey(obs)) {
						toAdd=histo.get(obs);
					}
					sum+=toAdd;
				}
				
				//avg
				double newProb = sum / numObs;
				
				histoBelief.put(obs, newProb);
			}			
		}
	}
	
	public Map<String, Double> getBelief(Map<String, Double> histo) {

		//easy exit
		if(histo==null)
			return histoBelief;
		
		//else recompute based on observation
		valueObservations.add(histo);
		updateBelief();
		return histoBelief;
	}
}
