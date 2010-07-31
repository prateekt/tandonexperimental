package api.tuxguitar.tg_import.player.impl.sequencer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import api.tuxguitar.tg_import.player.base.MidiPlayerException;
import api.tuxguitar.tg_import.player.base.MidiSequencer;
import api.tuxguitar.tg_import.player.base.MidiSequencerProvider;

public class MidiSequencerProviderImpl implements MidiSequencerProvider{
	
	private List sequencers;
	
	public MidiSequencerProviderImpl(){
		super();
	}
	
	public List listSequencers() throws MidiPlayerException {
		if(this.sequencers == null){
			this.sequencers = new ArrayList();
			this.sequencers.add(new MidiSequencerImpl());
		}
		return this.sequencers;
	}
	
	public void closeAll() throws MidiPlayerException {
		Iterator it = listSequencers().iterator();
		while(it.hasNext()){
			MidiSequencer sequencer = (MidiSequencer)it.next();
			sequencer.close();
		}
	}
	
}
