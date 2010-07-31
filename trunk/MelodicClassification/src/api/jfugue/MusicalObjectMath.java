package api.jfugue;

import java.util.*;

import util.math.dist.GaussianNoiseGenerator;

public class MusicalObjectMath {
	
	public static double computeDurScale(List<MusicalEvent> events) {
		List<MusicalEvent> noteEvents = MusicStringManipulator.getNoteMusicalEvents(events);
		MusicalObjectMath.obtainDistances(events);

		for(MusicalEvent e : noteEvents) {
			NoteMusicalEvent n = (NoteMusicalEvent) e;
			if(n.getDuration()!=0 && n.getTimeSinceLast()!=0) {
					return n.getTimeSinceLast() / n.getDuration();
			}
		}
		
		return 0;
	}
	
	public static void scaleRandomNoteDurations(List<MusicalEvent> events, double dur_scale_factor, double probOfChange) {
		List<MusicalEvent> noteEvents = MusicStringManipulator.getNoteMusicalEvents(events);
		double lastDiff=0;
		double scale_factor = computeDurScale(events);
		for(MusicalEvent e : noteEvents) {
			NoteMusicalEvent n  = (NoteMusicalEvent) e;
			
			//correct start time for last diff
			if(lastDiff!=0) {
				double toAdd = lastDiff * scale_factor;
				n.setTime((int)(n.getTime()+toAdd));
			}
			
			//randonly choose notes to enlongate
			if(Math.random() < probOfChange) {
				double newDuration = n.getDuration()*dur_scale_factor;
				lastDiff=newDuration - n.getDuration();
				n.setDuration(newDuration);
			}
			else {
				lastDiff=0;
			}
		}
	}
	
	public static void insertNoiseBetweenNoteEvents(List<MusicalEvent> events, int noise_center) {
		List<MusicalEvent> noteEvents = MusicStringManipulator.getNoteMusicalEvents(events);
		List<Integer> noisePoints = GaussianNoiseGenerator.genNoisePoints(noteEvents.size(), noise_center);
		int cumNoise=0;
		for(int x=0; x < noteEvents.size(); x++) {
			MusicalEvent e = noteEvents.get(x);
			int noise = noisePoints.get(x);
			e.setTime(e.getTime()+noise+cumNoise);
			cumNoise+=noise;
		}
	}
	
	public static Map<String, Integer> getNoteHistogram(List<String> notes) {
		Map<String, Integer> rtn = new TreeMap<String, Integer>();
		
		for(String note : notes) {
			
			if(!rtn.containsKey(note)) {
				rtn.put(note, 1);
			}
			else {
				int noteCount = rtn.get(note);
				noteCount++;
				rtn.put(note, noteCount);
			}			
		}
		
		return rtn;
	}

	public static List<Double> obtainNDistances(List<MusicalEvent> events, int n) {
		List<Double> rtn = new ArrayList<Double>();
		
		MusicalEvent trailer = events.get(0);
		for(int x=n; x < events.size(); x++) {
			double elem = events.get(x).getTime() - trailer.getTime();
			rtn.add(elem);
			trailer = events.get(x);
		}
		
		return rtn;
	}
	
	public static List<Double> obtainDistances(List<MusicalEvent> events) {
		List<Double> rtn = new ArrayList<Double>();
		
		MusicalEvent trailer = events.get(0);
		for(int x=1; x < events.size(); x++) {
			MusicalEvent e = events.get(x);
			double elem = e.getTime() - trailer.getTime();
			rtn.add(elem);
			trailer = e;
			
			//hack
			e.setTimeSinceLast((int)elem);
		}
		
		return rtn;		
	}	
}
