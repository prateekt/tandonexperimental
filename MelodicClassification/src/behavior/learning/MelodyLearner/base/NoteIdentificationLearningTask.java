package behavior.learning.MelodyLearner.base;

import java.util.*;

import behavior.learning.base.DistributionSetLearningTask;

import util.math.StringMath;

public class NoteIdentificationLearningTask extends DistributionSetLearningTask {
	
	public NoteIdentificationLearningTask() {
		super(StringMath.getNotes());
	}

	@Override
	public Map<String, Double> getBelief(Map<String, Double> noteHisto) {
		return super.getBelief(noteHisto);
	}	
}
