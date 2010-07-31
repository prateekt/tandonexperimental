package behavior.trainer;

import behavior.trainer.example.NoteDistancePerturbation;
import util.db.*;

public class TrainingExampleGeneratorImpl extends TrainingExampleGenerator{
	
	private static final int NUM_PER_TYPE=10;
	private static final int d_noiseCenter=10;
	private static final double dur_sf = 3;
	private static final double dur_p = 0.7;

	public TrainingExampleGeneratorImpl(DBManager midDB, String originalFile) {
		super(midDB, originalFile);
	}
	
	@Override
	protected void generationAlgorithm(String inputFile) {

		//compute paths
		String label = midDB.getLabel(inputFile);
		int index = midDB.getNextIndex(label);
		
		//generation of 10 distance perturbation examples
		for(int x=0; x < NUM_PER_TYPE;x++) {
			String outputFile = midDB.computeFilePath(label, index);
			NoteDistancePerturbation n = new NoteDistancePerturbation(inputFile, outputFile, d_noiseCenter);
			examples.add(n);
			index++;
		}
			
		//generation of 10 note duration perturbation examples
/*		for(int x=0; x < NUM_PER_TYPE; x++) {
			String outputFile = midDB.computeFilePath(label, index);
			NoteDurationPerturbation n = new NoteDurationPerturbation(inputFile, outputFile, dur_sf, dur_p);
			examples.add(n);
			index++;
		}*/
	}
}
