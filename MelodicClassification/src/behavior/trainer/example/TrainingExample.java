package behavior.trainer.example;

import java.util.List;

import api.jfugue.JFugueUtil;
import api.jfugue.MusicStringManipulator;
import api.jfugue.MusicalEvent;

public abstract class TrainingExample {
	
	/**
	 * Input midi file from temp
	 */
	private String inputFile;

	/**
	 * Output midi File name in midi_DB
	 */
	private String outputFile;	
	
	
	public TrainingExample(String inputFile, String outputFile) {
		this.inputFile = inputFile;
		this.outputFile = outputFile;
	}
	
	public void generate() {
		
		//load file
		String ms = JFugueUtil.loadMusicString(inputFile).trim();
		ms=MusicStringManipulator.t_normalize(ms);
		List<MusicalEvent> events = MusicStringManipulator.parseMusicalEvents(ms);
		
		//apply some abstract procedure on the events
		process_rule(events);
		
		//reconstruct music string and save to file
		String recon = MusicStringManipulator.reconstructMusicString(events);
		JFugueUtil.saveMusicString(outputFile, recon);
		
		System.out.println("Generating " + outputFile + " from " + inputFile + ".");
	}
	
	public abstract void process_rule(List<MusicalEvent> events);

	/**
	 * @return the inputFile
	 */
	public String getInputFile() {
		return inputFile;
	}

	/**
	 * @param inputFile the inputFile to set
	 */
	public void setInputFile(String inputFile) {
		this.inputFile = inputFile;
	}

	/**
	 * @return the outputFile
	 */
	public String getOutputFile() {
		return outputFile;
	}

	/**
	 * @param outputFile the outputFile to set
	 */
	public void setOutputFile(String outputFile) {
		this.outputFile = outputFile;
	}
}
