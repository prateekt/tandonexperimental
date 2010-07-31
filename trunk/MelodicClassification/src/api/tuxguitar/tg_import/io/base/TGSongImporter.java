package api.tuxguitar.tg_import.io.base;

import java.io.InputStream;

import api.tuxguitar.tg_import.song.factory.TGFactory;
import api.tuxguitar.tg_import.song.models.TGSong;

public interface TGSongImporter {
	
	public String getImportName();
	
	public TGFileFormat getFileFormat();
	
	public boolean configure(boolean setDefaults);
	
	public TGSong importSong(TGFactory factory,InputStream stream) throws TGFileFormatException;
	
}
