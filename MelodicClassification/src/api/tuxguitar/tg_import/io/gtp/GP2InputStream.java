package api.tuxguitar.tg_import.io.gtp;

import java.io.IOException;
import java.util.Iterator;

import api.tuxguitar.tg_import.io.base.TGFileFormat;
import api.tuxguitar.tg_import.song.managers.TGSongManager;
import api.tuxguitar.tg_import.song.models.TGBeat;
import api.tuxguitar.tg_import.song.models.TGChord;
import api.tuxguitar.tg_import.song.models.TGColor;
import api.tuxguitar.tg_import.song.models.TGDuration;
import api.tuxguitar.tg_import.song.models.TGMeasure;
import api.tuxguitar.tg_import.song.models.TGMeasureHeader;
import api.tuxguitar.tg_import.song.models.TGNote;
import api.tuxguitar.tg_import.song.models.TGNoteEffect;
import api.tuxguitar.tg_import.song.models.TGSong;
import api.tuxguitar.tg_import.song.models.TGString;
import api.tuxguitar.tg_import.song.models.TGTempo;
import api.tuxguitar.tg_import.song.models.TGText;
import api.tuxguitar.tg_import.song.models.TGTimeSignature;
import api.tuxguitar.tg_import.song.models.TGTrack;
import api.tuxguitar.tg_import.song.models.TGVelocities;
import api.tuxguitar.tg_import.song.models.TGVoice;
import api.tuxguitar.tg_import.song.models.effects.TGEffectBend;
import api.tuxguitar.tg_import.song.models.effects.TGEffectHarmonic;

/**
 * @author julian
 * 
 * TODO To change the template for this generated type comment go to Window - Preferences - Java - Code Style - Code Templates
 */
public class GP2InputStream extends GTPInputStream {
	
	private static final String SUPPORTED_VERSIONS[] = new String[]{ "FICHIER GUITAR PRO v2.20", "FICHIER GUITAR PRO v2.21" };
	
	private static final int TRACK_COUNT = 8;
	
	private static final short TRACK_CHANNELS[][] = new short[][]{
		new short[]{0,1},
		new short[]{2,3},
		new short[]{4,5},
		new short[]{6,7},
		new short[]{8,10},
		new short[]{11,12},
		new short[]{13,14},
		new short[]{9,9},
	};
	
	public GP2InputStream(GTPSettings settings){
		super(settings, SUPPORTED_VERSIONS);
	}
	
	public TGFileFormat getFileFormat(){
		return new TGFileFormat("Guitar Pro 2","*.gtp");
	}
	
	public TGSong readSong() throws GTPFormatException, IOException {
		readVersion();
		if (!isSupportedVersion(getVersion())) {
			this.close();
			throw new GTPFormatException("Unsuported Version");
		}
		TGSong song = getFactory().newSong();
		
		readInfo(song);
		
		int tempo = readInt();
		int tripletFeel = ((readInt() == 1)?TGMeasureHeader.TRIPLET_FEEL_EIGHTH:TGMeasureHeader.TRIPLET_FEEL_NONE);
		
		readInt(); //key
		
		for (int i = 0; i < TRACK_COUNT; i++) {
			TGTrack track = getFactory().newTrack();
			track.setNumber( (i + 1) );
			track.getChannel().setChannel(TRACK_CHANNELS[ i ][0]);
			track.getChannel().setEffectChannel(TRACK_CHANNELS[ i ][1]);
			TGColor.RED.copy(track.getColor());
			
			int strings = readInt();
			for (int j = 0; j < strings; j++) {
				TGString string = getFactory().newString();
				string.setNumber( j + 1 );
				string.setValue( readInt() );
				track.getStrings().add( string  );
			}
			song.addTrack(track);
		}
		
		int measureCount = readInt();
		
		for (int i = 0; i < TRACK_COUNT; i++) {
			readTrack(song.getTrack(i));
		}
		
		skip(10);
		
		TGMeasureHeader previous = null;
		long[] lastReadedStarts = new long[TRACK_COUNT];
		for (int i = 0; i < measureCount; i++) {
			TGMeasureHeader header = getFactory().newHeader();
			header.setStart( (previous == null)?TGDuration.QUARTER_TIME:(previous.getStart() + previous.getLength()) );
			header.setNumber( (previous == null)?1:previous.getNumber() + 1 );
			header.getTempo().setValue( (previous == null)?tempo:previous.getTempo().getValue() );
			header.setTripletFeel(tripletFeel);
			readTrackMeasures(song,header,lastReadedStarts);
			previous = header;
		}
		
		TGSongManager manager = new TGSongManager(getFactory());
		manager.setSong(song);
		manager.autoCompleteSilences();
		
		this.close();
		
		return song;
	}
	
	private void readInfo(TGSong song) throws IOException{
		song.setName(readStringByteSizeOfByte());
		song.setAuthor(readStringByteSizeOfByte());
		readStringByteSizeOfByte();
	}
	
	private TGDuration readDuration() throws IOException {
		TGDuration duration = getFactory().newDuration();
		duration.setValue( (int) (Math.pow( 2 , (readByte() + 4) ) / 4 ) );
		return duration;
	}
	
	private void readTrackMeasures(TGSong song,TGMeasureHeader header,long[] lastReadedStarts) throws IOException {
		readTimeSignature(header.getTimeSignature());
		
		skip(6);
		
		int[] beats = new int[TRACK_COUNT];
		for (int i = 0; i < TRACK_COUNT; i++) {
			readUnsignedByte();
			readUnsignedByte();
			beats[i] = readUnsignedByte();
			skip(9);
		}
		
		skip(2);
		
		int flags = readUnsignedByte();
		
		header.setRepeatOpen( ((flags & 0x01) != 0) );
		if ((flags & 0x02) != 0) {
			header.setRepeatClose( readUnsignedByte() );
		}
		
		if ((flags & 0x04) != 0) {
			header.setRepeatAlternative( parseRepeatAlternative(song, header.getNumber(), readUnsignedByte()) );
		}
		
		song.addMeasureHeader(header);
		for (int i = 0; i < TRACK_COUNT; i++) {
			TGTrack track = song.getTrack(i);
			TGMeasure measure = getFactory().newMeasure(header);
			
			long start = measure.getStart();
			for (int j = 0; j < beats[i]; j++) {
				long length = readBeat(track, measure,start,lastReadedStarts[i]);
				lastReadedStarts[i] = start;
				start += length;
			}
			
			track.addMeasure(measure);
		}
	}
	
	private void readTimeSignature(TGTimeSignature timeSignature) throws IOException {
		timeSignature.setNumerator(readUnsignedByte());
		timeSignature.getDenominator().setValue(readUnsignedByte());
	}
	
	private long readBeat(TGTrack track, TGMeasure measure, long start, long lastReadedStart) throws IOException {
		readInt();
		
		TGBeat beat = getFactory().newBeat();
		TGVoice voice = beat.getVoice(0);
		TGDuration duration = readDuration();
		TGNoteEffect effect = getFactory().newEffect();
		
		int flags1 = readUnsignedByte();
		int flags2 = readUnsignedByte();
		
		if ((flags2 & 0x02) != 0) {
			readMixChange(measure.getTempo());
		}
		
		if ((flags2 & 0x01) != 0) {
			readUnsignedByte(); //strokeType
			readUnsignedByte(); //strokeDuration
		}
		
		duration.setDotted( ((flags1 & 0x10) != 0) );
		if ((flags1 & 0x20) != 0) {
			duration.getTupleto().setEnters(3);
			duration.getTupleto().setTimes(2);
			skip(1);
		}
		
		// beat effects
		if ((flags1 & 0x04) != 0) {
			readBeatEffects(effect);
		}
		
		// chord diagram
		if ((flags1 & 0x02) != 0) {
			readChord(track.stringCount(), beat);
		}
		
		// text
		if ((flags1 & 0x01) != 0) {
			readText(beat);
		}
		
		if((flags1 & 0x40) != 0){
			if(lastReadedStart < start){
				TGBeat previousBeat = getBeat(track, measure, lastReadedStart);
				if(previousBeat != null){
					TGVoice previousVoice = previousBeat.getVoice(0);
					Iterator it = previousVoice.getNotes().iterator();
					while(it.hasNext()){
						TGNote previous = (TGNote)it.next();
						TGNote note = getFactory().newNote();
						note.setValue(previous.getValue());
						note.setString(previous.getString());
						note.setVelocity(previous.getVelocity());
						note.setTiedNote(true);
						
						voice.addNote(note);
					}
				}
			}
		}
		else if ((flags1 & 0x08) == 0) {
			int stringsFlags = readUnsignedByte();
			int effectsFlags = readUnsignedByte();
			int graceFlags = readUnsignedByte();
			
			for (int i = 5; i >= 0; i--) {
				if ((stringsFlags & (1 << i)) != 0) {
					TGNote note = getFactory().newNote();
					
					int fret = readUnsignedByte();
					int dynamic = readUnsignedByte();
					if ((effectsFlags & (1 << i)) != 0) {
						readNoteEffects(effect);
					}
					note.setValue( (fret >= 0 && fret < 100)?fret:0);
					note.setVelocity( (TGVelocities.MIN_VELOCITY + (TGVelocities.VELOCITY_INCREMENT * dynamic)) - TGVelocities.VELOCITY_INCREMENT );
					note.setString( track.stringCount() - i );
					note.setEffect(effect.clone(getFactory()));
					note.getEffect().setDeadNote(  (fret < 0 || fret >= 100)  );
					
					voice.addNote(note);
				}
				
				// Grace note
				if ((graceFlags & (1 << i)) != 0) {
					readGraceNote();
				}
			}
		}
		
		beat.setStart(start);
		voice.setEmpty(false);
		duration.copy(voice.getDuration());
		measure.addBeat(beat);
		
		return duration.getTime();
	}
	
	private void readText(TGBeat beat) throws IOException{
		TGText text = getFactory().newText();
		text.setValue(readStringByte(0));
		beat.setText(text);
	}
	
	private void readBeatEffects(TGNoteEffect effect) throws IOException {
		int flags = readUnsignedByte();
		effect.setVibrato( (flags == 1 || flags == 2) );
		effect.setFadeIn( (flags == 4) );
		effect.setTapping( (flags == 5) ) ;
		effect.setSlapping( (flags == 6) ) ;
		effect.setPopping( (flags == 7) ) ;
		if(flags == 3){
			readBend(effect);
		}
		else if(flags == 8 || flags == 9){
			TGEffectHarmonic harmonic = getFactory().newEffectHarmonic();
			harmonic.setType((flags == 8)?TGEffectHarmonic.TYPE_NATURAL:TGEffectHarmonic.TYPE_ARTIFICIAL);
			harmonic.setData(0);
			effect.setHarmonic(harmonic);
		}
	}
	
	private void readNoteEffects(TGNoteEffect effect) throws IOException {
		int flags = readUnsignedByte();
		effect.setHammer( (flags == 1 || flags == 2) );
		effect.setSlide( (flags == 3 || flags == 4) );
		if(flags == 5 || flags == 6){
			readBend(effect);
		}
	}
	
	private void readBend(TGNoteEffect effect) throws IOException {
		skip(6);
		float value = Math.max(  ((readUnsignedByte() / 8f) - 26f) , 1f);
		TGEffectBend bend = getFactory().newEffectBend();
		bend.addPoint(0,0);
		bend.addPoint(Math.round(TGEffectBend.MAX_POSITION_LENGTH / 2), Math.round(value * TGEffectBend.SEMITONE_LENGTH) );
		bend.addPoint(Math.round(TGEffectBend.MAX_POSITION_LENGTH),Math.round(value * TGEffectBend.SEMITONE_LENGTH));
		effect.setBend(bend);
		skip(1);
	}
	
	private void readGraceNote() throws IOException {
		byte bytes[] = new byte[3];
		read(bytes);
	}
	
	private void readTrack(TGTrack track) throws IOException {
		track.getChannel().setInstrument((short)readInt());
		readInt(); // Number of frets
		track.setName(readStringByteSizeOfByte());
		track.setSolo(readBoolean());
		track.getChannel().setVolume((short)readInt());
		track.getChannel().setBalance((short)readInt());
		track.getChannel().setChorus((short)readInt());
		track.getChannel().setReverb((short)readInt());
		track.setOffset(readInt());
	}
	
	private void readChord(int strings, TGBeat beat) throws IOException {
		TGChord chord = getFactory().newChord(strings);
		chord.setName(readStringByte(0));
		
		byte[] data = new byte[4];
		
		this.skip(1);
		this.read(data);
		
		if ((data[0] != 12) && ((data[3] & 0xc) != 12)) {
			skip(32);
		}
		
		chord.setFirstFret(readInt());
		if (chord.getFirstFret() != 0) {
			for (int i = 0; i < 6; i++) {
				int fret = readInt();
				if(i < chord.countStrings()){
					chord.addFretValue(i,fret);
				}
			}
		}
		if(chord.countNotes() > 0){
			beat.setChord(chord);
		}
	}
	
	private void readMixChange(TGTempo tempo) throws IOException {
		int flags = readUnsignedByte();
		// Tempo
		if ((flags & 0x20) != 0) {
			tempo.setValue(readInt());
			readUnsignedByte();
		}
		// Reverb
		if ((flags & 0x10) != 0) {
			readUnsignedByte();
			readUnsignedByte();
		}
		// Chorus
		if ((flags & 0x08) != 0) {
			readUnsignedByte();
			readUnsignedByte();
		}
		// Balance
		if ((flags & 0x04) != 0) {
			readUnsignedByte();
			readUnsignedByte();
		}
		// Volume
		if ((flags & 0x02) != 0) {
			readUnsignedByte();
			readUnsignedByte();
		}
		// Instrument
		if ((flags & 0x01) != 0) {
			readUnsignedByte();
		}
	}
	
	private int parseRepeatAlternative(TGSong song,int measure,int value){
		int repeatAlternative = 0;
		int existentAlternatives = 0;
		Iterator it = song.getMeasureHeaders();
		while(it.hasNext()){
			TGMeasureHeader header = (TGMeasureHeader)it.next();
			if(header.getNumber() == measure){
				break;
			}
			if(header.isRepeatOpen()){
				existentAlternatives = 0;
			}
			existentAlternatives |= header.getRepeatAlternative();
		}
		
		for(int i = 0; i < 8; i ++){
			if(value > i && (existentAlternatives & (1 << i)) == 0){
				repeatAlternative |= (1 << i);
			}
		}
		return repeatAlternative;
	}
	
	private TGBeat getBeat(TGTrack track, TGMeasure measure,long start){
		TGBeat beat = getBeat(measure,start);
		if(beat == null){
			for(int i = (track.countMeasures() - 1);i >=0; i-- ){
				beat = getBeat(track.getMeasure(i),start);
				if(beat != null){
					break;
				}
			}
		}
		return beat;
	}
	
	private TGBeat getBeat(TGMeasure measure,long start){
		if(start >= measure.getStart() && start < (measure.getStart() + measure.getLength())){
			Iterator beats = measure.getBeats().iterator();
			while(beats.hasNext()){
				TGBeat beat = (TGBeat)beats.next();
				if(beat.getStart() == start){
					return beat;
				}
			}
		}
		return null;
	}
}