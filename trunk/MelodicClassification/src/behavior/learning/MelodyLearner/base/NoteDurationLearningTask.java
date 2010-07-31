package behavior.learning.MelodyLearner.base;

import java.util.List;

import behavior.learning.base.DistributionLearningTask;
import behavior.transcriber.base.Note;


public class NoteDurationLearningTask {
	
	protected DistributionLearningTask[] durLT; //one for each note
	protected double[] durBelief;

	public NoteDurationLearningTask(int maxObsSize) {
		durLT = new DistributionLearningTask[maxObsSize];
		for(int x=0; x < durLT.length; x++) {
			durLT[x] = new DistributionLearningTask(null);
		}
		durBelief = new double[maxObsSize];
	}
	
	private void updateBelief(List<Note> obs) {
		
		//update LTs with their beliefs
		for(int x=0; x < obs.size(); x++) {
			double dur = obs.get(x).getDuration();
			durLT[x].getBelief(""+dur);
		}
		
		//extract my belief
		for(int x=0; x < durLT.length; x++) {
			String belief = durLT[x].getMLE();
			if(belief==null)
				durBelief[x] = 0.0;
			else
				durBelief[x] = Double.parseDouble(belief);
		}
	}
	
	public double[] getBelief(List<Note> obs) {

		if(obs==null) {
			return durBelief;
		}
		else {
			updateBelief(obs);
			return durBelief;
		}
	}	
}
