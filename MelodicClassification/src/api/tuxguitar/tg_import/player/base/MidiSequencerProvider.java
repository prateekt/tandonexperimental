package api.tuxguitar.tg_import.player.base;

import java.util.List;

public interface MidiSequencerProvider {
	
	public List listSequencers() throws MidiPlayerException;
	
	public void closeAll() throws MidiPlayerException;
	
}
