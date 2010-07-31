package behavior.trainer;
import java.util.*;

import behavior.trainer.example.TrainingExample;

import util.db.DBManager;

public abstract class TrainingExampleGenerator {
	
	protected DBManager midDB;
	protected List<TrainingExample> examples;
	protected String originalFile;
	
	protected TrainingExampleGenerator(DBManager midDB, String originalFile) {
		examples = new ArrayList<TrainingExample>();
		this.originalFile = originalFile;
		this.midDB = midDB;
	}
			
	protected abstract void generationAlgorithm(String trainingFile);
	
	public void generateExamples() {
					
		//use generation algorithm to generate audio examples
		generationAlgorithm(originalFile);
		
		//generate all training examples
		for(TrainingExample e : examples) {
			e.generate();
		}
	}
			
	/**
	 * @param o
	 * @return
	 * @see java.util.List#add(java.lang.Object)
	 */
	public boolean addTrainingExample(TrainingExample o) {
		return examples.add(o);
	}

	/**
	 * @param index
	 * @return
	 * @see java.util.List#remove(int)
	 */
	public TrainingExample removeTrainingExample(int index) {
		return examples.remove(index);
	}

	/**
	 * @return
	 * @see java.util.List#size()
	 */
	public int getNumTrainingExamples() {
		return examples.size();
	}

	
	
	/**
	 * @param index
	 * @return
	 * @see java.util.List#get(int)
	 */
	public TrainingExample getTrainingExample(int index) {
		return examples.get(index);
	}
}
