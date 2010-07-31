package api.jfugue;

import java.util.List;

import behavior.transcriber.base.Melody;
import behavior.transcriber.base.Note;


public class MelodyToJFugue {

	public static String convert_pitch_melody(Melody m) {
		String rtn="";
		List<Note> notes = m.getNotes();
		for(Note n : notes) {
			String jFugueNote="";
			String tNoteName = n.getName();
			if(tNoteName.indexOf("SILENCE") > -1)
				continue;
			String[] tok  = tNoteName.split("_");
			String jFugueNoteName = tok[0] + tok[1];
			if(n.getDuration()!=-1) {
				double duration = getDuration(n.getDuration());
				jFugueNote = jFugueNoteName + "/0.25";
			}
			else {
				jFugueNote = jFugueNoteName;
			}
			rtn+= jFugueNote + " ";
		}
		
		return rtn.trim();
		
	}
	
	public static double getDuration(int duration) {
		
		//2 duration units correspond to eightnote (1/8)
		return (duration/2.0) * 1.0/8.0;		
	}
	
	public static String convert_chroma_basic(Melody m) {
		String rtn="";
		for(Note n : m.getNotes()) {
			rtn = rtn + n.getName() + " ";
		}
		rtn = rtn.trim();
		return rtn;
	}
}
