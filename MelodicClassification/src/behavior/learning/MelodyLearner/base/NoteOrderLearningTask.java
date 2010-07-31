package behavior.learning.MelodyLearner.base;
import java.util.*;

import behavior.learning.base.DistributionLearningTask;
import behavior.transcriber.base.*;

import util.math.StringMath;

public class NoteOrderLearningTask {
	
	protected DistributionLearningTask[] noteLT;
	protected String[] orderBelief;
	
	public NoteOrderLearningTask(int maxObsSize) {
		noteLT = new DistributionLearningTask[maxObsSize];
		for(int x=0; x < noteLT.length; x++) {
			noteLT[x] = new DistributionLearningTask(StringMath.getNotes());
		}
		orderBelief = new String[maxObsSize];
	}
	
	private void updateBelief(List<Note> obs) {
		
		//update LTs with their beliefs
		for(int x=0; x < obs.size(); x++) {
			String note = obs.get(x).getName();
			noteLT[x].getBelief(note);
		}
		
		//extract my belief
		for(int x=0; x < noteLT.length; x++) {
			orderBelief[x] = noteLT[x].getMLE();
		}
	}
	
	public String[] getBelief(List<Note> obs) {

		if(obs==null) {
			return orderBelief;
		}
		else {
			updateBelief(obs);
			return orderBelief;
		}
	}	
}
