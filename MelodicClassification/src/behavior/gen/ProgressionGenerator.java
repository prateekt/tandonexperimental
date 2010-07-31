package behavior.gen;
import java.util.*;

import behavior.transcriber.base.Note;


public class ProgressionGenerator {
	
	public static String TWELVE_BAR_BLUES = "1 1 1 1 4 4 1 1 5 4 1 1";
	
	public static void main(String[] args) {
		for(Note n : generateProgression("C", TWELVE_BAR_BLUES)) {
			System.out.println(n.getName());
		}
	}
	
	public static List<Note> generateProgression(String key, String formula) {
		return MajorBasedGenerator.generateScale(key, formula);
	}
	
}
