package api.tuxguitar;

import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Iterator;
import java.util.List;

import api.tuxguitar.tg_import.io.base.TGSongLoader;
import api.tuxguitar.tg_import.io.gtp.GP4OutputStream;
import api.tuxguitar.tg_import.io.gtp.GTPSettings;
import api.tuxguitar.tg_import.io.midi.MidiSongExporter;
import api.tuxguitar.tg_import.song.factory.TGFactory;
import api.tuxguitar.tg_import.song.models.TGBeat;
import api.tuxguitar.tg_import.song.models.TGDuration;
import api.tuxguitar.tg_import.song.models.TGMeasure;
import api.tuxguitar.tg_import.song.models.TGNote;
import api.tuxguitar.tg_import.song.models.TGSong;
import api.tuxguitar.tg_import.song.models.TGTrack;
import api.tuxguitar.tg_import.song.models.TGVoice;

public class TuxGuitarUtil {

	public static TGSong loadSong(String file) {
		TGSongLoader l = new TGSongLoader();
		try {
			return l.load(new TGFactory(), new FileInputStream(file));
		}
		catch(Exception e) {
			System.out.println("Song could not be loaded. See error trace.");
			e.printStackTrace();
			return null;
		}
	}
	
	public static void exportToMidi(String file, TGSong song) {
		MidiSongExporter e = new MidiSongExporter();
		try {
			e.exportSong(new BufferedOutputStream(new FileOutputStream(file)), song);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	public static void exportToGP4(String file, TGSong song) {
		GP4OutputStream g = new GP4OutputStream(new GTPSettings());
		try {
			g.init(new TGFactory(), new BufferedOutputStream(new FileOutputStream(file)));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		g.writeSong(song);
	}
	
	public static TGSong produceVariant(TGSong song) {
		TGSong newSong = song.clone(new TGFactory());
		
		Iterator<TGTrack> tracks = song.getTracks();
		int t=0;
		Iterator<TGMeasure> measures = song.getTrack(t).getMeasures();
		int m=0;
		
		while(measures.hasNext() && song.getTrack(t).getMeasure(m)!=null && m < 3) {
			int off=0;
			List beats = song.getTrack(t).getMeasure(m).getBeats();
			for(int i = 0; i < beats.size(); i++) {
				TGBeat beat = (TGBeat) beats.get(i);
				int length = 0;
				int index = 0;
				System.out.println(beat.getStart());
				for(int x = 0; x < beat.countVoices(); x++)
				{
					TGVoice voice = beat.getVoice(x);
					List notes = voice.getNotes();
					for(int z = 0; z < notes.size(); z++)
					{
						TGNote n = (TGNote) notes.get(z);
						TGDuration duration = voice.getDuration();
						if(beat.isRestBeat())
						{
							//rest
						}
						else
						{
							if(voice.getNotes().size()> 1) {
								//chord
							}
							else {
	
								TGMeasure measure = newSong.getTrack(t).getMeasure(m);									
								TGBeat beat1 = measure.getBeat(i);									
								TGVoice voice1 = beat.getVoice(x);
								TGNote note1 = voice1.getNote(z);
								
								TGDuration dur = voice1.getDuration();
								int original = dur.getValue();
								int halfDuration = getHalfDuration(dur);
								dur.setValue(halfDuration);
								measure.duplicateBeat(i+off);
								off++;
								System.out.println(m + " " + song.getTrack(t).getMeasure(m).countBeats()+ ":::" +measure.countBeats());
							}
						}
					}
				}
			}	
			m++;
		}
		
		return newSong;
	}

	public static int getDoubleDuration(TGDuration dur) {
		int val = dur.getValue();
		switch(val) {
			case TGDuration.HALF:
				return TGDuration.WHOLE;
			case TGDuration.QUARTER:
				return TGDuration.HALF;
			case TGDuration.EIGHTH:
				return TGDuration.QUARTER;
			case TGDuration.SIXTEENTH:
				return TGDuration.EIGHTH;
			case TGDuration.THIRTY_SECOND: 
				return TGDuration.SIXTEENTH;
			default:
				return TGDuration.SIXTY_FOURTH;
		}
	}

	public static int getHalfDuration(TGDuration dur) {
		int val = dur.getValue();
		switch(val) {
			case TGDuration.WHOLE:
				return TGDuration.HALF;
			case TGDuration.HALF:
				return TGDuration.QUARTER;
			case TGDuration.QUARTER:
				return TGDuration.EIGHTH;
			case TGDuration.EIGHTH:
				return TGDuration.SIXTEENTH;
			case TGDuration.SIXTEENTH:
				return TGDuration.THIRTY_SECOND;
			case TGDuration.THIRTY_SECOND: 
				return TGDuration.SIXTY_FOURTH;
			default:
				return TGDuration.SIXTY_FOURTH;
		}
	}
	
	public static TGSong isolateTrack(TGSong song, int t) {
		TGSong rtn = song.clone(new TGFactory());
		rtn.clearTracks();
		TGTrack track = song.getTrack(t);
		rtn.addTrack(track);
		return rtn;
	}
	
	public static TGSong getNMeasures(TGSong song, int n) {
		TGSong rtn = song.clone(new TGFactory());
		TGTrack track  = rtn.getTrack(0);
/*		track.clear();
		rtn.clearMeasureHeaders();
		for(int m=0; m < n; m++) {
			track.addMeasure(song.getTrack(0).getMeasure(m));
			rtn.addMeasureHeader(song.getMeasureHeader(m));
		}
		rtn.addMeasureHeader(song.getMeasureHeader(song.countMeasureHeaders()-1));
		track.addMeasure(song.getTrack(0).getMeasure(song.getTrack(0).countMeasures()-1));
		
		System.out.println(song.countMeasureHeaders());
		System.out.println(rtn.getTrack(0).countMeasures());
		System.out.println(rtn.countMeasureHeaders());
		System.out.println(track.countMeasures());*/
		track.removeMeasure(4);
		rtn.removeMeasureHeader(4);
		return rtn;		
	}
	
	public static void main(String[] args) {
		TGSong song = loadSong("test1.gp4");
//		TGSong newSong = produceVariant(song);
		exportToMidi("Test.mid", song);
//		TGSong newSong = isolateTrack(song,2);
//		TGSong newSong = getNMeasures(song, 3);
//		System.out.println(newSong.getTrack(0).countMeasures());
//		exportToGP4("test1.gp4", newSong);
	}		
}
