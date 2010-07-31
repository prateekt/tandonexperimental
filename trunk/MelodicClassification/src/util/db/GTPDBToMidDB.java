package util.db;

import java.io.File;

import behavior.trainer.TrainingExampleGenerator;
import behavior.trainer.TrainingExampleGeneratorImpl;



import api.tuxguitar.TuxGuitarUtil;
import api.tuxguitar.tg_import.song.models.TGSong;

public class GTPDBToMidDB extends DBConverter {
	
	private DBManager tempDB;
	
	public GTPDBToMidDB(DBManager gtpDB, DBManager midDB, DBManager tempDB) {
		super(gtpDB, midDB);
		this.tempDB = tempDB;
	}

	@Override
	public void handleConversion() {
		File[] files = db1.getDBFiles();
		for(File f : files) {
			convert(f);
		}		
	}
	
	public void convert(File f) {
		
		//load TG Song
		TGSong song = TuxGuitarUtil.loadSong(f.getAbsolutePath());
		
		//generate temp midi
		String label = db1.getLabel(f.getName());
		String tempMidiFileName = tempDB.computeFilePath(label);
		TuxGuitarUtil.exportToMidi(tempMidiFileName, song);
		
		//generate training examples off of that midi file
		//this includes saving to midi db
		TrainingExampleGenerator g = new TrainingExampleGeneratorImpl(db2, tempMidiFileName);
		g.generateExamples();		
	}
}
