package util.math;
import java.util.*;

import behavior.transcriber.base.Note;


public class StringMath {
	
	private static String[] notes = {"C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B"};
	private static String[] noteMask = {"C", "Q", "D", "W", "E", "F", "R", "G", "T", "A", "Y", "B"};
	private static String[] guitarStringBaseNotes = {"E", "B", "G", "D", "A", "E"};
	
	public static String getGuitarNote(int string, int fret, boolean useMask) {
		
		//compute note in non-mask frame
		String realBaseNote = guitarStringBaseNotes[string];
		String note=realBaseNote;
		for(int x=0; x < fret; x++) {
			note = getHalfStepUp(note,false);
		}
		
		//return
		if(useMask) {
			int index = StringMath.getNoteIndex(note, false);
			return noteMask[index];
		}
		else {
			return note;
		}
	}
		
	public static String[] getNotes() {
		return notes;
	}
	
	public static String getHalfStepDown(String note, boolean useMask) {
		return noteFrom(note, 11, useMask);
	}
	
	public static String getWholeStepUp(String note, boolean useMask) {
		return noteFrom(note, 2, useMask);
	}
	
	public static String getHalfStepUp(String note, boolean useMask) {
		return noteFrom(note,1,useMask);
	}
	
	public static String getNote(int index, boolean useMask) {
		if(useMask)
			return noteMask[index];
		else
			return notes[index];
	}
	
	public static int subtractFromIndex(int origIndex, int sub) {
		int rtn = origIndex - sub;
		if(rtn < 0) {
			rtn = rtn + 12;
		}
		return rtn;
	}
	
	public static int addToIndex(int origIndex, int add) {
		int rtn = origIndex + add;
		if(rtn >= 12) {
			rtn = rtn - 12;
		}
		return rtn;
	}
		
	public static int compareDistHashes(String hash1, String hash2) {
		int iter = Math.min(hash1.length(), hash2.length());
		int totalDifference=0;
		for(int x=0; x < iter; x++) {
			String diff1Str = ""+hash1.charAt(x);
			String diff2Str = ""+hash2.charAt(x);
			
			int diff1 = Integer.parseInt(diff1Str);
			int diff2 = Integer.parseInt(diff2Str);
			
			int dist = Math.abs(diff1-diff2);
			totalDifference+=dist;	
		}
		
		return totalDifference;		
	}
	
	public static String getDistanceHash(String maskSeq) {
		String rtn="";
		String trailNote = ""+maskSeq.charAt(0);
		for(int x=1; x < maskSeq.length(); x++) {
			String currentNote = ""+maskSeq.charAt(x);
			int dist = StringMath.getTranspositionDistance(trailNote, currentNote, true);
			rtn+=""+dist;
			trailNote = currentNote;
		}
		return rtn;
	}
	
	public static String transpose(String maskSeq, int steps) {
		String rtn = "";
		for(int x=0; x < maskSeq.length(); x++) {
			String current = "" + maskSeq.charAt(x);
			int index = getNoteIndex(current, true);
			int newIndex = addToIndex(index, steps);
			rtn+= noteMask[newIndex];
		}
		return rtn;
	}
	
	public static List<Note> toNotesAgain(String maskSeq) {
		List<Note> rtn  = new ArrayList<Note>();
		for(int x=0; x < maskSeq.length(); x++) {
			String current = "" + maskSeq.charAt(x);
			int index = getNoteIndex(current, true);
			Note n = new Note(notes[index], -1, null);
			rtn.add(n);
		}
		return rtn;
	}
	
	public static String toNoteMask(List<Note> notes) {
		String rtn = "";
		for(Note n : notes) {
			int index = getNoteIndex(n.getName(), false);
			rtn+= noteMask[index];
		}
		return rtn;
	}
	
	public static int getSequenceTDistance(String seq1, String seq2, boolean useMask) {
		int iter = Math.min(seq1.length(), seq2.length());
		
		int rtn =0;
		for(int x=0; x < iter; x++) {
			rtn+= getTranspositionDistance(""+seq1.charAt(x), ""+seq2.charAt(x), useMask);
		}
		
		return rtn;		
	}

	public static int getTranspositionDistance_signed(String note1, String note2, boolean useMask) {
		int index1 = getNoteIndex(note1,useMask);
		int index2 = getNoteIndex(note2,useMask);
		
		int distance = index2 - index1;
		if(distance >= 6) {
			distance = 12 - distance;
		}
		
		return distance;		
	}

	
	public static int getTranspositionDistance(String note1, String note2, boolean useMask) {
		int index1 = getNoteIndex(note1,useMask);
		int index2 = getNoteIndex(note2,useMask);
		
		int distance = Math.abs(index2 - index1);
		if(distance >= 6) {
			distance = Math.abs(12 - distance);
		}
		
		return distance;		
	}
	
	public static String noteFrom(String note, int steps, boolean useMask) {
		int index = getNoteIndex(note, useMask);
		int newIndex = addToIndex(index, steps);
		return getNote(newIndex,useMask);
	}	

	public static int getNoteIndex(String note, boolean useMask) {
		int index=-1;
		for(int x=0; x < notes.length; x++) {
			if(!useMask && note.trim().equals(notes[x])) {
				index = x;
			}
			if(useMask && note.trim().equals(noteMask[x])) {
				index = x;
			}
		}
		
		return index;
	}
	
	public static int LevenshteinDistance(String s, String t) {
		int m = s.length();
		int n = t.length();
		
		// d is a table with m+1 rows and n+1 columns
		int[][] d = new int[m+1][n+1];
	  
		for(int i = 0; i < m; i++)
			d[i][0] = i; // deletion
		for(int j=0; j < n; j++)
			d[0][j] = j; // insertion
	  
	   for(int j=1; j < n; j++) {
		   for(int i=1; i < m; i++) {
			   if(s.charAt(i) == t.charAt(j)) { 
				   d[i][j] = d[i-1][j-1];
			   }
		       else {
		    	   d[i][j] = Math.min(d[i-1][j]+1,
		    			   	 Math.min(d[i][j-1]+1, d[i-1][j-1]+1));
		       }
		   }
	   }
	   
	   if(m==0 || n==0) {
		   System.out.println(s + " ::: " + t + " FAIL");
		   return -1;
	   }
	   
	   return d[m-1][n-1];
	 }
	
	public static void main(String[] args) {
		System.out.println(StringMath.isLengthRepairable("JI", "J3I"));
	}
		
	public static boolean isLengthRepairable(String rel, String unrel) {
		int relIndex=0;
		for(char c : unrel.toCharArray()) {
			if(c==rel.charAt(relIndex)) {
				relIndex++;
				
				//easy exit
				if(relIndex==rel.length()) {
					return true;
				}
			}
		}
		
		return false;
	}
	
	public static int LevenshteinDistance_delOnly(String s, String t) {
		int m = s.length();
		int n = t.length();
		
		// d is a table with m+1 rows and n+1 columns
		int[][] d = new int[m+1][n+1];
	  
		for(int i = 0; i < m; i++)
			d[i][0] = i; // deletion
		for(int j=0; j < n; j++)
			d[0][j] = j; // insertion
	  
	   for(int j=1; j < n; j++) {
		   for(int i=1; i < m; i++) {
			   if(s.charAt(i) == t.charAt(j)) { 
				   d[i][j] = d[i-1][j-1];
			   }
		       else {
		    	   d[i][j] = Math.min(d[i-1][j]+1,d[i][j-1]+1);
		       }
		   }
	   }
	   
	   if(m==0 || n==0) {
		   System.out.println(s + " ::: " + t + " FAIL");
		   return -1;
	   }
	   
	   return d[m-1][n-1];
	 }
}