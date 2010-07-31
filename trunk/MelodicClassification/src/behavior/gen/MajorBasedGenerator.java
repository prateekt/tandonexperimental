package behavior.gen;
import java.util.*;

import behavior.transcriber.base.Note;


public class MajorBasedGenerator {
	
	public static String MAJOR_PENTATONIC_SCALE = "1 2 3 5 6";
	public static String MINOR_PENTATONIC_SCALE = "1 b3 4 5 b7";
	public static String MAJOR_TRIAD = "1 3 5";
	public static String MINOR_TRIAD = "1 b3 5";
	
	
	public static List<Note> generateScale(String key, String formula) {
		List<Note> rtn = new ArrayList<Note>();
		
		//Generate major scale
		List<Note> majorScale = BasicScaleGenerator.generateBasicScale(key, BasicScaleGenerator.MAJOR_SCALE);
		
		//split and use formula
		String[] toks = formula.split(" ");
		for(String tok : toks) {
			if(tok.indexOf("b") > -1) {
				
				//add flated note
				int index = Integer.parseInt(tok.substring(1,2)) - 1;
				String noteStr = majorScale.get(index).getName() + "b";
				Note toAdd = new Note(noteStr, -1, null);
				rtn.add(toAdd);
			}
			else {
		
				//add reg note
				int index = Integer.parseInt(tok.substring(0,1)) - 1;
				String noteStr = majorScale.get(index).getName();
				Note toAdd = new Note(noteStr, -1, null);
				rtn.add(toAdd);				
			}
		}
		
		return rtn;		
	}

}
