package api.jfugue;
import java.util.*;

public class MusicStringManipulator {
	
//	private static final int NORMALIZATION_CONSTANT = 20;
	private static final int NORMALIZATION_CONSTANT = 10;
	
	public static List<String> extractNotes(String musicString) {
		List<String> rtn = new ArrayList<String>();

		String[] toks = musicString.split(" ");
		for(int x=0; x < toks.length; x++) {
			String tok = toks[x];
			if(tok.indexOf("/") > -1) {
				tok = tok.trim();
				String note = tok.substring(0, tok.indexOf("/"));
				rtn.add(note);
			}
		}
		
		return rtn;		
	}
	
	public static List<MusicalEvent> getNoteMusicalEvents(List<MusicalEvent> events) {
		List<MusicalEvent> rtn = new ArrayList<MusicalEvent>();
		for(MusicalEvent evt : events) {
			if(evt instanceof NoteMusicalEvent) {
				rtn.add(evt);
			}
		}
		return rtn;
	}
	
	public static String reconstructMusicString(List<MusicalEvent> events) {
		String rtn="";
		
		if(events!=null && events.size() > 0) {
			
			for(MusicalEvent e : events) {
				rtn+= e + " ";
			}
			
/*			rtn+= events.get(0).getHeadContent() + " ";
			
			for(int x=1; x < events.size(); x++) {
				MusicalEvent event = events.get(x);
				rtn+= "@" + event.getTime() + event.getContent() + " ";
			}*/
		}
		
		return rtn.trim();
	}
		
	public static List<MusicalEvent> parseMusicalEvents(String musicString) {
		List<MusicalEvent> rtn = new ArrayList<MusicalEvent>();

		String[] toks = musicString.split(" ");
		String accumString="";
		String headContent = "";
		boolean firstTokenSet=false;
		int timeStamp=-1;
		for(int x=0; x < toks.length; x++) {
			String tok = toks[x];
			
			if(tok.indexOf("@") > -1 && !firstTokenSet) {
				
				//handle head
				MusicalEvent e = new MusicalEvent(-1, accumString);
				rtn.add(e);
				e.setHeadContent(headContent);
				firstTokenSet=true;
				timeStamp = Integer.parseInt(tok.substring(tok.indexOf("@")+1));
				accumString="";
			}
			else if(tok.indexOf("@") > -1) {
	
				//add musical event
				if(accumString.indexOf("/") > -1) {
					int slashDex = accumString.indexOf("/");
					int aDex = accumString.indexOf("a", slashDex);
					int dDex = accumString.indexOf("d", aDex);
					String note = accumString.substring(slashDex-3, slashDex);
					double duration = Double.parseDouble(accumString.substring(slashDex+1, aDex));
					int attackVel = Integer.parseInt(accumString.substring(aDex+1, dDex));
					int speedVel = Integer.parseInt(accumString.substring(dDex+1,accumString.length()));
					NoteMusicalEvent e = new NoteMusicalEvent(timeStamp, accumString, note,duration,attackVel, speedVel);
					rtn.add(e);
				}
				else {
					MusicalEvent e = new MusicalEvent(timeStamp, accumString);
					rtn.add(e);
				}
				
				//finally -reset accumstr, set next timestamp
				accumString="";			
				tok = tok.trim();
				timeStamp = Integer.parseInt(tok.substring(tok.indexOf("@")+1));
				
			}
			else if(x==toks.length-1) {
				accumString = accumString + " " + toks[x].trim();				

				//add musical event
				if(accumString.indexOf("/") > -1) {
					int slashDex = accumString.indexOf("/");
					int aDex = accumString.indexOf("a", slashDex);
					int dDex = accumString.indexOf("d", aDex);
					String note = accumString.substring(slashDex-3, slashDex);
					double duration = Double.parseDouble(accumString.substring(slashDex+1, aDex));
					int attackVel = Integer.parseInt(accumString.substring(aDex+1, dDex));
					int speedVel = Integer.parseInt(accumString.substring(dDex+1,accumString.length()));
					NoteMusicalEvent e = new NoteMusicalEvent(timeStamp, accumString, note,duration,attackVel, speedVel);
					rtn.add(e);
				}
				else {
					MusicalEvent e = new MusicalEvent(timeStamp, accumString);
					rtn.add(e);
				}
			}
			else {

				//first token setting
				if(!firstTokenSet) {
					headContent += toks[x].trim();
				}
				
				//always
				accumString = accumString + " " + toks[x].trim();				
			}
		}
				
		return rtn;
	}
	
	public static String t_scale(String musicString, int factor) {

		//manip
		String[] toks = musicString.split(" ");
		for(int x=0; x < toks.length; x++) {
			String tok = toks[x];
			if(tok.indexOf("@") > -1) {
				tok = tok.trim();
				int number = Integer.parseInt(tok.substring(tok.indexOf("@")+1));
				number = number/factor;
				toks[x] = "@" + number;
			}
		}
		
		//reconstruct
		String recon="";
		for(int x=0; x < toks.length; x++) {
			recon = recon + toks[x] + " ";
		}
		recon = recon.trim();
		return recon;
	}
	
	public static String t_normalize(String musicString) {
		String newString = ""+musicString;
		return t_scale(newString, NORMALIZATION_CONSTANT);
	}
}