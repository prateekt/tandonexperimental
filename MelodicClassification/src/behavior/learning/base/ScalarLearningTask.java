package behavior.learning.base;

import java.util.ArrayList;
import java.util.List;

public class ScalarLearningTask {

	protected List<Double> valueObservations;
	protected double scalarBelief;
	
	public ScalarLearningTask() {
		valueObservations = new ArrayList<Double>();
		scalarBelief = 0;
	}

	private void updateBelief() {
		
		//easy exit
		if(valueObservations.size()==0) {
			scalarBelief = 0;
		}
		else {
			//compute mean
			double sum=0.0;
			for(double d : valueObservations) {
				sum+= d;
			}
			scalarBelief = sum/valueObservations.size();
		}
	}
	
	public double getBelief(double newObservation) {
		
		//easy exit
		if(newObservation==-1) {
			return scalarBelief;
		}
		else {
			//recompute length belief
			valueObservations.add(newObservation);
			updateBelief();
			return scalarBelief;
		}
	}		
}
