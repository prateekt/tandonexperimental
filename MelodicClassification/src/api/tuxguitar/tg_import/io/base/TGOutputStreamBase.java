package api.tuxguitar.tg_import.io.base;

import java.io.IOException;
import java.io.OutputStream;

import api.tuxguitar.tg_import.song.factory.TGFactory;
import api.tuxguitar.tg_import.song.models.TGSong;

public interface TGOutputStreamBase {
	
	public void init(TGFactory factory,OutputStream stream);
	
	public boolean isSupportedExtension(String extension);
	
	public TGFileFormat getFileFormat();
	
	public void writeSong(TGSong song) throws TGFileFormatException,IOException;
}
