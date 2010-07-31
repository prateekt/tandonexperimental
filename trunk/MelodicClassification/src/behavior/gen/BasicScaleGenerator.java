package behavior.gen;
import java.util.*;

import behavior.transcriber.base.Note;


import util.math.StringMath;

public class BasicScaleGenerator {
	
	public static String MAJOR_SCALE = "WWHWWWH";
	public static String MINOR_SCALE = "WHWWHWW";
	public static String HARMONIC_MINOR_SCALE = "WHWWHHW";
	public static String IONIAN_SCALE = "WWWHWWW";
	public static String LYDIAN_SCALE = "WWWHWWH";
	public static String MIXOLYDIAN_SCALE = "WWHWWHW";
	public static String AEOLIAN_SCALE = "WHWWHWW";
	public static String DORIAN_SCALE = "WHWWWHW";
	public static String PHYRGIAN_SCALE = "HWWWHWW";
	public static String LOCRIAN_SCALE = "HWWHWWW";
		
	public static Map<String, List<Note>> generateAllBasicScales() {
		Map<String, List<Note>> rtn = new LinkedHashMap<String, List<Note>>();
		
		//generate based on base notes
		String[] baseNotes = StringMath.getNotes();
		for(int x=0; x < baseNotes.length; x++) {
			String baseNote = baseNotes[x];
			List<Note> major = generateBasicScale(baseNote, MAJOR_SCALE);
			rtn.put(baseNote + "_MAJOR_SCALE", major);
			List<Note> minor = generateBasicScale(baseNote, MINOR_SCALE);
			rtn.put(baseNote + "_MINOR_SCALE", minor);
			List<Note> harmonicMinor = generateBasicScale(baseNote, HARMONIC_MINOR_SCALE);
			rtn.put(baseNote + "_HARMONIC_MINOR_SCALE", harmonicMinor);
			List<Note> ionian = generateBasicScale(baseNote, IONIAN_SCALE);
			rtn.put(baseNote + "_IONIAN_SCALE", ionian);
			List<Note> lydian = generateBasicScale(baseNote, LYDIAN_SCALE);
			rtn.put(baseNote + "_LYDIAN_SCALE", lydian);
			List<Note> mixolydian = generateBasicScale(baseNote, MIXOLYDIAN_SCALE);
			rtn.put(baseNote + "_MIXOLYDIAN_SCALE", mixolydian);
			List<Note> aeolian = generateBasicScale(baseNote, AEOLIAN_SCALE);
			rtn.put(baseNote + "_AEOLIAN_SCALE", aeolian);
			List<Note> dorian = generateBasicScale(baseNote, DORIAN_SCALE);
			rtn.put(baseNote + "_DORIAN_SCALE", dorian);
			List<Note> phyrgian = generateBasicScale(baseNote, PHYRGIAN_SCALE);
			rtn.put(baseNote + "_PHYRGIAN_SCALE", phyrgian);
			List<Note> locrian = generateBasicScale(baseNote, LOCRIAN_SCALE);
			rtn.put(baseNote + "_LOCRIAN_SCALE", locrian);
		}
		
		return rtn;
	}
	
	public static List<Note> generateBasicScale (String start, String formula) {
		List<Note> rtn = new ArrayList<Note>();
		
		//add starting note
		Note startNote = new Note(start, -1, null);
		rtn.add(startNote);

		//obey formula
		String lastNote = start;
		for(char c : formula.toCharArray()) {
			String currentNote = "";
			if(c=='W') {
				currentNote = StringMath.getWholeStepUp(lastNote, false);
			}			
			else if(c=='H') {
				currentNote = StringMath.getHalfStepUp(lastNote, false);
			}
			
			Note newNote = new Note(currentNote, -1, null);
			rtn.add(newNote);
			lastNote = currentNote;
		}
		return rtn;
	}	
}
