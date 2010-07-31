package behavior.learning.MelodyLearner.base;
import behavior.learning.base.DistributionLearningTask;
public class MelodyLengthLearningTask extends DistributionLearningTask {
		
	public MelodyLengthLearningTask() {
		super(null);
	}
		
	public int getBelief(int newObservation) {
		super.getBelief(""+newObservation);
		return Integer.parseInt(this.getMLE());
	}	
}
