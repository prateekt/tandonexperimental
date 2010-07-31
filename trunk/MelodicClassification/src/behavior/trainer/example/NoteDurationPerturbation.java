package behavior.trainer.example;

import java.util.List;


import api.jfugue.MusicalEvent;
import api.jfugue.MusicalObjectMath;

public class NoteDurationPerturbation extends TrainingExample {
	
	private double durScaleFactor;
	private double probOfChange;
	
	public NoteDurationPerturbation(String inputFile, String outputFile, double durScaleFactor, double probOfChange) {
		super(inputFile, outputFile);
		this.durScaleFactor = durScaleFactor;
		this.probOfChange = probOfChange;
	}

	@Override
	public void process_rule(List<MusicalEvent> events) {
		MusicalObjectMath.scaleRandomNoteDurations(events, durScaleFactor, probOfChange);		
	}
}
