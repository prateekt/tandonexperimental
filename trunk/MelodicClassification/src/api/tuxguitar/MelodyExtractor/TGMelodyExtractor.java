package api.tuxguitar.MelodyExtractor;

import api.tuxguitar.TuxGuitarUtil;
import api.tuxguitar.tg_import.song.factory.TGFactory;
import api.tuxguitar.tg_import.song.models.*;
import behavior.transcriber.base.Melody;
import behavior.transcriber.base.Note;

import java.io.File;
import java.util.*;

import util.math.StringMath;

public class TGMelodyExtractor {
	
	public static TGSong song;
	
	public static void main(String[] args) {
//		List<Melody> melodies = extractMelodies("db/db_gtp/jm.gp4",0,0);		//based on rests
//		Map<String, List<List<TGMeasure>>> melodyMeasuresMap = extractMelodyMeasures("db/db_gtp/starry_night.gp4",0,0,0);			
//		TuxGuitarUtil.exportToMidi("Test.mid", sepSongs.get(0));
//		TuxGuitarUtil.exportToGP4("Test.gp4", sepSongs.get(4));
	}
	
	public static List<TGSong> getMelodiesAsSongs(Map<String,List<List<TGMeasure>>> melodyMeasuresMap) {

		List<TGSong> sepSongs = new ArrayList<TGSong>();
		TGFactory factory = new TGFactory();
		for(String key : melodyMeasuresMap.keySet()) {
			List<List<TGMeasure>> melodyMeasures = melodyMeasuresMap.get(key);
			System.out.println("KEY: "  + key);
			int x=0;
			for(List<TGMeasure> mel : melodyMeasures) {
				System.out.println("MEL " + x + ": ");
				TGSong new_song = factory.newSong();
				sepSongs.add(new_song);
				TGTrack new_track = factory.newTrack();
				new_track.setStrings(song.getTrack(0).getStrings());
				new_song.addTrack(new_track);
				for(TGMeasure m : mel) {
					new_track.addMeasure(m);
					System.out.println(m);
				}
				x++;
			}
		}
		
		//populate measure headers
		for(TGSong song : sepSongs) {
			int maxMeasures=-1;
			for(int x=0; x < song.countTracks(); x++) {
				int numMeasures = song.getTrack(x).countMeasures();
				if(numMeasures > maxMeasures) {
					maxMeasures = numMeasures;
				}
			}
			
			//generate measure headers
			for(int x=0; x < maxMeasures; x++) {
				song.addMeasureHeader(factory.newHeader());
			}
		}
		
		return sepSongs;
	}
	
	public static Map<String,List<List<TGMeasure>>> extractMelodyMeasures(String songFile, int minTrack, int maxTrack, int voice) {
		Map<String, List<List<TGMeasure>>> rtn = new LinkedHashMap<String, List<List<TGMeasure>>>();
		
		//get segments
		Map<String,List<List<TGBeat>>> segmentsMap = getSegments(songFile,minTrack,maxTrack);
		for(String key : segmentsMap.keySet()) { //based on track
			List<List<TGMeasure>> melodiesMeasures = new ArrayList<List<TGMeasure>>();

			//find track that corresponds in name
			TGTrack track = null;
			for(int x=minTrack; x <= maxTrack; x++) {
				track = song.getTrack(x);
				if(key.equalsIgnoreCase(song.getTrack(x).getName())) {
					track = song.getTrack(x);
					break;
				}
			}
			
			//create lists of measures
			List<List<TGBeat>> melodiesBeats = segmentsMap.get(key);
			for(List<TGBeat> melodyBeats  :melodiesBeats) {
				List<TGMeasure> melodyMeasures = new ArrayList<TGMeasure>();
				
				//iterate through beats
				for(TGBeat beat : melodyBeats) {
					
					//search measures for beat
					measureSearch:for(int x=0; x < track.countMeasures(); x++) {
						TGMeasure current = track.getMeasure(x);
						if(current.getBeats().contains(beat) && !melodyMeasures.contains(current)) {
							melodyMeasures.add(current);
							break measureSearch;
						}
					}
				}
				
				//add to global measures
				melodiesMeasures.add(melodyMeasures);
			}
			
			rtn.put(key, melodiesMeasures);
		}
		
		return rtn;
	}
		
	public static Map<String,List<Melody>> extractMelodies(String songFile, int minTrack, int maxTrack, int voice) {
		Map<String, List<Melody>> rtn = new LinkedHashMap<String, List<Melody>>();
		int numMel=0;
		
		//file root
		File f = new File(songFile);
		String root = f.getName().substring(0,f.getName().indexOf("."));
		
		//get segments
		Map<String,List<List<TGBeat>>> segmentsMap = getSegments(songFile,minTrack,maxTrack);
		for(String key : segmentsMap.keySet()) {
			List<Melody> melList = new ArrayList<Melody>();
			List<List<TGBeat>> segments = segmentsMap.get(key);
			
			//populate return structure
			for(List<TGBeat> segment : segments) {
				List<TGNote> notesList = getNotes(segment, voice);
				List<Note> notes = getNotes(notesList); //notes of riff
				Melody mellie = new Melody(notes, root + "_" +key + "_" + numMel);
				melList.add(mellie);
				numMel++;
			}
			
			rtn.put(key, melList);
		}
		
		return rtn;
	}
		
	public static List<Note> getNotes(List<TGNote> list) {
		List<Note> rtn = new ArrayList<Note>();
		for(TGNote n : list) {
			String noteName = getNoteName(n);
			Note note = new Note(noteName,-1,null);
			rtn.add(note);
		}
		return rtn;
	}
	
	public static String getNoteName(TGNote n) {
		int string = n.getString();
		int fret = n.getValue();
		return StringMath.getGuitarNote(string-1, fret, false);
	}
	
	public static List<TGNote> getNotes(List<TGBeat> beats, int voice) {
		List<TGNote> rtn = new ArrayList<TGNote>();
		for(TGBeat beat : beats) {
			TGVoice v = beat.getVoice(voice);
			for(Object n : v.getNotes()) {
				rtn.add((TGNote)n);
			}
		}
		return rtn;
	}
	
	public static Map<String,List<List<TGBeat>>> getSegments(String file, int minTrack, int maxTrack) {
		Map<String,List<List<TGBeat>>> rtn = new LinkedHashMap<String,List<List<TGBeat>>>();
		
		song = TuxGuitarUtil.loadSong(file);
		Map<String,List<TGMeasure>> measuresMap = extractAllMeasures(song, minTrack, maxTrack);
		for(String key : measuresMap.keySet()) {
			List<TGMeasure> measures = measuresMap.get(key);
			List<TGBeat> beats = getBeats(measures);

			//segmentation based on rest clumps
			beats = clumpRests(beats);
			List<List<TGBeat>> results = segment_rests(beats);
			
			rtn.put(key, results);
		}

		return rtn;
	}
	
	public static List<List<TGBeat>> segment_rests(List<TGBeat> beats) {
		List<List<TGBeat>> rtn = new ArrayList<List<TGBeat>>();
		
		List<TGBeat> toAdd = new ArrayList<TGBeat>();
		for(int x=0; x < beats.size(); x++) {
			TGBeat b = beats.get(x);
			
			if(b.isRestBeat()) {
				if(x!=0) {
					rtn.add(toAdd);
					toAdd = new ArrayList<TGBeat>();
				}
			}
			else if(x==beats.size()-1) {
				toAdd.add(b);
				rtn.add(toAdd);
			}
			else {
				toAdd.add(b);
			}			
		}
		
		return rtn;
	}
	
	public static List<TGBeat> clumpRests(List<TGBeat> beats) {
		List<TGBeat> rtn = new ArrayList<TGBeat>();
		
		boolean restFound = false;
		for(TGBeat b : beats) {
			
			if(b.isRestBeat() && !restFound) {
				restFound = true;
				rtn.add(b);
			}
			else if(b.isRestBeat() && restFound) {
				//do nothing
			}
			else if(!b.isRestBeat() && !restFound) {
				rtn.add(b);
			}
			else if(!b.isRestBeat() && restFound) {
				rtn.add(b);
				restFound = false;
			}	
		}
		return rtn;
	}
	
	public static List<TGBeat> getBeats(List<TGMeasure> mList) {
		List<TGBeat> rtn = new ArrayList<TGBeat>();
		for(int x=0; x < mList.size(); x++) {
			TGMeasure m = mList.get(x);
			for(Object b : m.getBeats()) {
				rtn.add((TGBeat)b);
			}
		}
		return rtn;
	}

	public static Map<String, List<TGMeasure>> extractAllMeasures(TGSong song, int minTrack, int maxTrack) {
		Map<String, List<TGMeasure>> rtn = new LinkedHashMap<String,List<TGMeasure>>();
		for(int x=minTrack; x <= maxTrack && x < song.countTracks(); x++) {
			TGTrack t = song.getTrack(x);
			String name = t.getName();
			List<TGMeasure> measures = extractMeasures(song, x, 0, t.countMeasures()-1);
			rtn.put(name, measures);
		}
		return rtn;
	}

	public static List<TGMeasure> extractMeasures(TGSong song, int track, int min, int max) {
		TGTrack t = song.getTrack(track);
		List<TGMeasure> rtn = new ArrayList<TGMeasure>();
		for(int x=min; x <= max; x++) {
			rtn.add(t.getMeasure(x));
		}
		return rtn;
	}
	
	public static List<Melody> getUniqueMelodies(List<Melody> melodies) {
		List<Melody> rtn = new ArrayList<Melody>();
		List<String> cache = new ArrayList<String>();
		for(Melody m : melodies) {
			if(!cache.contains(m.compareStr())) {
				rtn.add(m);
				cache.add(m.compareStr());
			}
		}
		return rtn;		
	}
}