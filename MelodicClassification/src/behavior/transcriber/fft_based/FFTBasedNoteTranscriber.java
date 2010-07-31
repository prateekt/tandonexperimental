package behavior.transcriber.fft_based;
import java.util.*;

import behavior.transcriber.base.Note;

import util.sys.SysUtil;

public class FFTBasedNoteTranscriber {
	
	public static void main(String[] args) {
		FFTBasedNoteTranscriber t = new FFTBasedNoteTranscriber();
		List<Double> arr = t.getClipArray();
		Map<String, Double> noteProb = t.genNoteProb(arr);
		for(String key : noteProb.keySet()) {
			System.out.println(key + " " + noteProb.get(key));
		}
	}
	
	public List<Note> transcribe(List<Double> arr) {
		List<Note> rtn = new ArrayList<Note>();
		for(Double d : arr) {
			String noteStr = this.getNearestNote(d);
			Note note = new Note(noteStr, 1, null);
			rtn.add(note);
		}
		return rtn;
	}
	
	public Map<String, Double> genNoteProb(List<Double> arr) {
		
		//generate histogram
		Map<String, Double> histo = new HashMap<String, Double>();
		for(Double d : arr) {
			String note = getNearestNote(d);
			if(!histo.containsKey(note)) {
				histo.put(note, 1.0);
			}
			else {
				histo.put(note, histo.get(note)+1);
			}
		}
		
		//turn into prob table
		for(String key : histo.keySet()) {
			histo.put(key, histo.get(key)/(arr.size()));
		}
		
		return histo;
	}
	
	public List<Double> getClipArray() {
		List<Double> temp = SysUtil.getArray("db/db_unlabeled_clips/test.txt");
		return zeroless(temp);
	}
	
	public List<Double> zeroless(List<Double> arr) {
		List<Double> rtn = new ArrayList<Double>();
		for(Double d : arr) {
			if(d!=0)
				rtn.add(d);
		}
		
		return rtn;
	}
	
	public String getNearestNote(double d) {
		
		String nearest = "";
		double minDist = Double.POSITIVE_INFINITY;
		Map<String,Double> noteTable = getNoteTable();
		for(String note : noteTable.keySet()) {
			double freq = noteTable.get(note);
			double dist = Math.abs(d - freq);
			if(dist < minDist) {
				minDist = dist;
				nearest = note;
			}
		}
		
		return nearest;		
	}
	
	public static Map<String, Double> getNoteTable() {
		Map<String, Double> rtn = new HashMap<String,Double>();
		rtn.put("C5", 523.24);
		rtn.put("C5#", 554.37);
		rtn.put("D5", 587.33);
		rtn.put("D5#", 622.25);
		rtn.put("F5", 698.46);
		rtn.put("F5#", 739.99);
		rtn.put("G5",783.99);
		rtn.put("G5#", 830.61);
		rtn.put("A5", 880.00);
		rtn.put("A5#", 932.33);
		rtn.put("B5", 987.77);
		rtn.put("C6", 1046.5);
		rtn.put("C6#", 1108.7);
		rtn.put("D6",1174.7);
		rtn.put("D6#",1244.5);
		rtn.put("E6", 1318.5);
		rtn.put("F6", 1396.9);
		rtn.put("F6#", 1480.0);
		rtn.put("G6", 1568.0);
		rtn.put("G6#", 1661.2);
		rtn.put("A6", 1760.0);
		rtn.put("A6#", 1864.7);
		rtn.put("B6", 1979.5);
		rtn.put("C7", 2093.0);
		rtn.put("C7#", 2217.5);
		rtn.put("D7", 2349.3);
		return rtn;
	}
}