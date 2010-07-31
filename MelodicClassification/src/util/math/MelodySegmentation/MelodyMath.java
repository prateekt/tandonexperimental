package util.math.MelodySegmentation;

import java.util.*;

import behavior.gen.BasicScaleGenerator;
import behavior.gen.MajorBasedGenerator;
import behavior.transcriber.base.Melody;
import behavior.transcriber.base.Note;




import util.math.StringMath;

public class MelodyMath {
	
	public static Map<Melody, Double> extractBigrams(Melody m) {
		Map<Melody, Double> rtn = new LinkedHashMap<Melody,Double>();
		List<Note> melodyNotes = m.getNotes();
		
		//easy exit
		if(melodyNotes.size() < 2)
			return rtn;
		
		//get bigrams
		List<Melody> bigrams = new ArrayList<Melody>();
		Note trail = null;
		for(int x=0; x < melodyNotes.size(); x++) {
			Note current = melodyNotes.get(x);		
			if(trail!=null) {
				Melody mNew = new Melody();
				mNew.addNote(trail);
				mNew.addNote(current);
				bigrams.add(mNew);
			}
		}
		
		//generate histogram
		int numNotes = bigrams.size();
		for(Melody bigram : bigrams) {
			
			//search for bigram
			Melody found = null;
			for(Melody bigram2 : rtn.keySet()) {
				if(bigram2.compareStr().equalsIgnoreCase(bigram.compareStr())) {
					found = bigram2;
					break;
				}
			}
			
			//update histogram
			if(found==null) {
				rtn.put(bigram, 1.0/numNotes);
			}
			else {
				rtn.put(found, rtn.get(found)+ 1.0/numNotes);
			}			
		}
		
		return rtn;
	}
	
	public static Map<Integer, Double> extractMelodicDirectionHistogram(Melody m) {
		Map<Integer, Double> rtn = new LinkedHashMap<Integer,Double>();
		
		//easy exit
		if(m.getNotes().size() < 2)
			return rtn;
		
		//generate intermediate directions string
		List<Note> melodyNotes = m.getNotes();
		String dirString = "";
		Note trail = null;
		for(Note n : melodyNotes) {
			if(trail!=null) {
				int dist = StringMath.getTranspositionDistance_signed(trail.getName(), n.getName(), false);
				boolean signPositive = (dist >= 0);
				
				if(signPositive) {
					dirString+="+";
				}
				else {
					dirString+="-";
				}
			}			
		}
		
		//dir count
		int dirCtr=1;
		char lastDir = dirString.charAt(0);
		for(int x=1; x < dirString.length();x++) {
			char currentDir = dirString.charAt(x);
			if(currentDir!=lastDir) {
				
				//add to count map
				if(!rtn.containsKey(dirCtr)) {
					rtn.put(dirCtr, 1.0);
				}
				else {
					rtn.put(dirCtr, rtn.get(dirCtr)+1);
				}
				
				//reset
				dirCtr=0;
				
			}
			else if(x==dirString.length()-1) {
				//add to count map
				if(!rtn.containsKey(dirCtr)) {
					rtn.put(dirCtr+1, 1.0);
				}
				else {
					rtn.put(dirCtr+1, rtn.get(dirCtr)+1);
				}				
			}
						
			//always
			dirCtr++;
			lastDir = currentDir;
		}
		
		//convert to histogram
		int numElem = rtn.keySet().size();
		if(numElem!=0) {
			for(int key : rtn.keySet()) {
				rtn.put(key, rtn.get(key)/numElem);
			}
		}
		
		return rtn;		
	}
	
	public static Map<Integer, Double> extractIntervalHistogram(Melody m) {
		Map<Integer, Double> rtn = new LinkedHashMap<Integer, Double>();
		
		//count intervals
		List<Note> melodyNotes = m.getNotes();
		Note trail = null;
		for(Note n : melodyNotes) {
			if(trail!=null) {
				int dist = StringMath.getTranspositionDistance_signed(trail.getName(), n.getName(), false);
				
				if(!rtn.containsKey(dist)) {
					rtn.put(dist, 1.0);
				}
				else {
					rtn.put(dist, rtn.get(dist)+1);
				}				
			}
			
			trail = n;
		}
		
		//convert to histogram
		int numIntervals = rtn.keySet().size();
		if(numIntervals!=0) {
			for(int key : rtn.keySet()) {
				rtn.put(key, rtn.get(key)/numIntervals);
			}
		}
		
		return rtn;
	}
	
	public static Map<String,Double> extractNoteHistogram(Melody m) {
		Map<String, Double> rtn = new LinkedHashMap<String,Double>();
		
		//count notes
		List<Note> melodyNotes = m.getNotes();
		for(Note n : melodyNotes) {
			
			if(!rtn.containsKey(n.getName())) {
				rtn.put(n.getName(), 1.0);
			}
			else {
				rtn.put(n.getName(), rtn.get(n.getName())+1.0);
			}			
			
		}
		
		//convert to histo
		int numNotes = rtn.keySet().size();
		if(numNotes!=0) {
			for(String key : rtn.keySet()) {
				rtn.put(key, rtn.get(key)/numNotes);
			}
		}
		
		return rtn;
	}
		
	public static List<Note> computeUniversalNotes(List<Note> majorProgression, boolean triadBased) {
		List<List<Note>> majorTriads = new ArrayList<List<Note>>();
		
		//generate triads
		for(Note n : majorProgression) {
			List<Note> majorTriad;

			if(triadBased)
				majorTriad = MajorBasedGenerator.generateScale(n.getName(), MajorBasedGenerator.MAJOR_TRIAD);
			else
				majorTriad = BasicScaleGenerator.generateBasicScale(n.getName(), BasicScaleGenerator.MAJOR_SCALE);
			
			majorTriads.add(majorTriad);
		}
		
		//compute overlaps
		List<Note> overlap= majorTriads.get(0);
		for(List<Note> triad : majorTriads) {
			overlap = getOverlap(overlap, triad);
		}
		
		return overlap;
	}
	
	public static String getMostProbableScale(List<Note> observations, Map<String, List<Note>> scaleDB) {
		int maxOverlap = Integer.MIN_VALUE;
		String rtn = "";
		for(String key : scaleDB.keySet()) {
			List<Note> scale = scaleDB.get(key);
			int ov = overlap(observations, scale);
			if(ov > maxOverlap) {
				maxOverlap = ov;
				rtn = key;
			}
		}
		return rtn;
	}
	
	public static List<Note> getOverlap(List<Note> l1, List<Note> l2) {
		List<Note> rtn = new ArrayList<Note>();
		Set<String> noteSet = new TreeSet<String>();
		for(Note n1 : l1) {
			for(Note n2 : l2 ){
				if(n1.getName().equals(n2.getName()) && !noteSet.contains(n1.getName())) {
					rtn.add(n1);
					noteSet.add(n1.getName());
				}
			}
		}
		
		return rtn;
	}
	
	public static int overlap(List<Note> l1, List<Note> l2) {
		int count=0;
		for(Note n1 : l1) {
			for(Note n2 : l2) {
				if(n1.getName().equals(n2.getName())) {
					count++;
				}
			}
		}
		
		return count;
	}
	
	public static List<Melody> generateAllTranspositions(Melody m) {
		List<Melody> rtn = new ArrayList<Melody>();
		
		String mask = StringMath.toNoteMask(m.getNotes());
		String originalName = m.getName();
		String originalStartingNote = m.getNotes().get(0).getName();
		for(int x=0; x < 12; x++) {
			String transpose_masked = StringMath.transpose(mask, x);
			List<Note> notes = StringMath.toNotesAgain(transpose_masked);
			String key = StringMath.noteFrom(originalStartingNote, x, false);
			Melody newMelody = new Melody(notes, originalName + "_" + key);
			rtn.add(newMelody);
		}
		
		return rtn;
	}	
}
