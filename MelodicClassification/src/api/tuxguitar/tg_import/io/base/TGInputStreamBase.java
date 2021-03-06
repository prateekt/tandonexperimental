package api.tuxguitar.tg_import.io.base;

import java.io.IOException;
import java.io.InputStream;

import api.tuxguitar.tg_import.song.factory.TGFactory;
import api.tuxguitar.tg_import.song.models.TGSong;

public interface TGInputStreamBase {
	
	public void init(TGFactory factory,InputStream stream);
	
	public boolean isSupportedVersion();
	
	public TGFileFormat getFileFormat();
	
	public TGSong readSong() throws TGFileFormatException,IOException;
}
