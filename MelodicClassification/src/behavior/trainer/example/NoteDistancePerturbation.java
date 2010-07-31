package behavior.trainer.example;

import java.util.List;


import api.jfugue.MusicalEvent;
import api.jfugue.MusicalObjectMath;

public class NoteDistancePerturbation extends TrainingExample {

	private int noiseCenter;
	
	public NoteDistancePerturbation(String inputFile, String outputFile, int noiseCenter) {
		super(inputFile, outputFile);
		this.noiseCenter = noiseCenter;
	}

	@Override
	public void process_rule(List<MusicalEvent> events) {
		MusicalObjectMath.insertNoiseBetweenNoteEvents(events,noiseCenter);
	}
}
