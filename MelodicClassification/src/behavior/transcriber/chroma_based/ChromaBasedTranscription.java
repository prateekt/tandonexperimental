package behavior.transcriber.chroma_based;
import java.util.*;

import behavior.transcriber.base.Note;

public class ChromaBasedTranscription {
	
	private List<Note> notes;
	private List<Note> pitchTranscription;
	
	public ChromaBasedTranscription(List<Note> notes, List<Note> pitchTranscription) {
		this.notes = notes;
		this.pitchTranscription = pitchTranscription;
	}
	
	public ChromaBasedTranscription(List<Note> notes) {
		this.notes = notes;
	}

	public String toString_pt() {
		StringBuffer rtn = new StringBuffer();
		rtn.append("[");
		for(int x=0; x < pitchTranscription.size(); x++) {
			Note n = pitchTranscription.get(x);
			rtn.append(n.getName());
/*			rtn.append(n.getName() + "(" + n.getDuration() + ") " + n.getMaxIntensity());*/
			if(x!=pitchTranscription.size()-1)
				rtn.append(" ");				
		}
		rtn.append("]");
		
		return rtn.toString();
	}

	@Override
	public String toString() {
		StringBuffer rtn = new StringBuffer();
		rtn.append("[");
		for(int x=0; x < notes.size(); x++) {
			Note n = notes.get(x);
			rtn.append(n.getName() + "(" + n.getDuration() + ") " + n.getMaxIntensity());
			if(x!=notes.size()-1)
				rtn.append(", ");
				
		}
		rtn.append("]");
		
		return rtn.toString();
	}

	/**
	 * @return the notes
	 */
	public List<Note> getNotes() {
		return notes;
	}

	/**
	 * @param notes the notes to set
	 */
	public void setNotes(List<Note> notes) {
		this.notes = notes;
	}

	/**
	 * @return the pitchTranscription
	 */
	public List<Note> getPitchTranscription() {
		return pitchTranscription;
	}

	/**
	 * @param pitchTranscription the pitchTranscription to set
	 */
	public void setPitchTranscription(List<Note> pitchTranscription) {
		this.pitchTranscription = pitchTranscription;
	}	
}
