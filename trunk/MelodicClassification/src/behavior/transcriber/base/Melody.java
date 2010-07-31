package behavior.transcriber.base;
import java.io.Serializable;
import java.util.*;

import util.math.StringMath;


public class Melody implements Serializable {
	
	private List<Note> notes;
	private String name;
	
	public String compareStr() {
		String rtn = "[";
		for(Note n : notes) {
			rtn+=n.getName() + " ";
		}
		rtn = rtn.trim() + "]";
		return rtn;		
	}
	
	public String maskSeqStr() {
		return StringMath.toNoteMask(notes);
	}
	
	@Override
	public String toString() {
		String rtn = name + " [";
		for(Note n : notes) {
			rtn+=n.getName() + " ";
//			rtn+="("+n.getMaxIntensity()+")";
		}
		rtn = rtn.trim() + "]";
//		rtn = rtn + "(" + name + ")"+"]";
		return rtn;
	}
	
	public Melody(String name) {
		notes = new ArrayList<Note>();
		this.name = name;
	}
	
	public Melody() {
		notes = new ArrayList<Note>();
		this.name = "";
	}
	
	public Melody(List<Note> notes, String name) {
		this.notes = notes;
		this.name = name;
	}
	
	public void addNote(Note n) {
		notes.add(n);
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
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}	
}
