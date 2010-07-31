package behavior.learning.MelodyLearner;
import java.util.*;

import behavior.learning.MelodyLearner.base.*;
import behavior.transcriber.base.Melody;
import behavior.transcriber.base.Note;

public class MelodyLearningTask {
	
	//melody LT parameters
	protected String melodyName;
	protected Melody myBelief;
	
	//sub learning tasks
	protected MelodyLengthLearningTask lengthLT;
	protected NoteDurationLearningTask noteDurLT;
	protected NoteOrderLearningTask noteOrderLT;
	
	//user feedback flags
	protected boolean lengthBeliefCorrect = false;
	protected boolean durBeliefCorrect = false;
	protected boolean notesBeliefCorrect = false;
	
	public MelodyLearningTask(String melodyName, int maxNumNotes) {
		this.melodyName = melodyName;
		lengthLT = new MelodyLengthLearningTask();
		noteDurLT = new NoteDurationLearningTask(maxNumNotes);
		noteOrderLT = new NoteOrderLearningTask(maxNumNotes);
		myBelief = new Melody(melodyName);
	}
	
	private void updateBelief(Melody m) {
		List<Note> mNotes = m.getNotes();
		
		//vars
		int lengthBelief = -1;
		String[] noteOrder = null;
		double[] durations = null;
		
		//update length belief
		if(lengthBeliefCorrect) {
			lengthBelief = lengthLT.getBelief(-1);
		}
		else {
			lengthBelief = lengthLT.getBelief(mNotes.size());
		}
		
		//update notes belief
		if(notesBeliefCorrect) {
			noteOrder = noteOrderLT.getBelief(null);
		}
		else {
			noteOrder = noteOrderLT.getBelief(mNotes);
		}
		
		//update durations belief
		if(durBeliefCorrect) {
			durations = noteDurLT.getBelief(null);	
		}
		else {
			durations = noteDurLT.getBelief(mNotes);
		}
		
		//update my belief
		myBelief = new Melody(melodyName);
		for(int x=0; x < lengthBelief; x++) {
			String noteName = noteOrder[x];
			double noteDur = durations[x];
			Note newNote = new Note(noteName, (int)noteDur, null);
			myBelief.addNote(newNote);
		}
	}
	
	public Melody getBelief(Melody newObservation) {
		
		//easy exit
		if(newObservation==null) {
			return myBelief;
		}
		else {
			updateBelief(newObservation);
			return myBelief;
		}
	}

	/**
	 * @return the lengthBeliefCorrect
	 */
	public boolean isLengthBeliefCorrect() {
		return lengthBeliefCorrect;
	}

	/**
	 * @param lengthBeliefCorrect the lengthBeliefCorrect to set
	 */
	public void setLengthBeliefCorrect(boolean lengthBeliefCorrect) {
		this.lengthBeliefCorrect = lengthBeliefCorrect;
	}

	/**
	 * @return the durBeliefCorrect
	 */
	public boolean isDurBeliefCorrect() {
		return durBeliefCorrect;
	}

	/**
	 * @param durBeliefCorrect the durBeliefCorrect to set
	 */
	public void setDurBeliefCorrect(boolean durBeliefCorrect) {
		this.durBeliefCorrect = durBeliefCorrect;
	}

	/**
	 * @return the notesBeliefCorrect
	 */
	public boolean isNotesBeliefCorrect() {
		return notesBeliefCorrect;
	}

	/**
	 * @param notesBeliefCorrect the notesBeliefCorrect to set
	 */
	public void setNotesBeliefCorrect(boolean notesBeliefCorrect) {
		this.notesBeliefCorrect = notesBeliefCorrect;
	}
}