package api.tuxguitar.tg_import.io.midi;

import java.io.OutputStream;

import api.tuxguitar.tg_import.io.base.TGFileFormat;
import api.tuxguitar.tg_import.io.base.TGSongExporter;
import api.tuxguitar.tg_import.player.base.MidiSequenceParser;
import api.tuxguitar.tg_import.song.managers.TGSongManager;
import api.tuxguitar.tg_import.song.models.TGSong;

public class MidiSongExporter implements TGSongExporter{
	
	private MidiSettings settings;
	
	public MidiSongExporter() {
		settings = new MidiSettings();
	}
	
	public String getExportName() {
		return "Midi";
	}
	
	public TGFileFormat getFileFormat() {
		return new TGFileFormat("Midi","*.mid;*.midi");
	}
	
	public boolean configure(boolean setDefaults) {
		this.settings = (MidiSettings.getDefaults());
		return (this.settings != null);
	}
	
	public void exportSong(OutputStream stream, TGSong song) {
		TGSongManager manager = new TGSongManager();
		manager.setSong(song);
		MidiSequenceParser parser = new MidiSequenceParser(manager,MidiSequenceParser.DEFAULT_EXPORT_FLAGS,100,this.settings.getTranspose());
		MidiSequenceHandlerImpl sequence = new MidiSequenceHandlerImpl( (song.countTracks() + 1) , stream);
		parser.parse(sequence);
	}
}
