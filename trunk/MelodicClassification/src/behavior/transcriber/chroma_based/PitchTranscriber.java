package behavior.transcriber.chroma_based;
import java.util.*;

import behavior.transcriber.base.Note;


public class PitchTranscriber {
	
	private static double INTENSITY_MIN=0;
	
	public static List<Note> getNotes(List<Integer> list, List<Double> intensityList) {
		
		List<Note> rtn = new ArrayList<Note>();
		
		int durCtr=0;
		int last=-1;
		List<Double> intensity = new ArrayList<Double>();

		for(int x=0; x < list.size(); x++) {
			int current = list.get(x);
			double currentIntensity = intensityList.get(x);
			
			//store if note period ended
			if(current!=last && last!=-1) {
				String noteName = getNoteName(last);
				Note n = new Note(noteName, durCtr, intensity);
				rtn.add(n);
				durCtr=0;
			}
			
			if(x==list.size()-1 && durCtr!=0) {
				String noteName = getNoteName(current);
				intensity = new ArrayList<Double>();
				intensity.add(currentIntensity);
				Note n = new Note(noteName,durCtr+1,intensity);
				rtn.add(n);
			}
			
			//always
			intensity.add(currentIntensity);
			durCtr++;
			last=current;
		}
		
		return rtn;
	}
	
	public static String getNoteName(int pitch) {
		int base_pitch_index = pitch % 12;
		String base_pitch = getNoteName_base(base_pitch_index);
		int octave = pitch / 12;
		return base_pitch + "_" + octave;
	}

	public static String getNoteName_base(int note) {
		String[] notes = {"C", "C#","D","D#","E","F","F#","G","G#","A","A#","B"};
		return notes[note];
	}	

}
