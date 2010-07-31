package release.DBScoreFollower;

import java.util.List;
import java.util.concurrent.BlockingQueue;

import release.DBLoader.FileBasedMelodyDB;
import release.DBLoader.UserSetting;
import release.DBRecognizer.DBRecognizerGUI;

import behavior.follower.MCS.AcceptablePath;
import behavior.follower.MCS.MCSBasedScoreFollower;
import behavior.transcriber.base.Melody;
import behavior.transcriber.base.Note;
import behavior.transcriber.chroma_based.ChromaBasedTranscription;
import behavior.transcriber.chroma_based.ChromaNoteTranscriber;

public class RecognizerThread implements Runnable {

	private BlockingQueue<String> queue;
	private Melody stream;
	private UserSetting settings;
	
	public RecognizerThread(BlockingQueue<String> queue, UserSetting settings) {
		this.queue = queue;
		this.settings = settings;
		stream = new Melody();
	}	
	
	public void run() {		
		
		while(true) {
			//take
			String file="";
			try {
				file = queue.take();
			}
			catch(Exception e) {
				e.printStackTrace();
			}
			String[] inputFiles = {file};
			
			//transcribe
			List<ChromaBasedTranscription> list = ChromaNoteTranscriber.transcribe(DBRecognizerGUI.RECORD_DIR+"/", inputFiles, DBRecognizerGUI.CHROMA_CLASSIFY_MATLAB_FILE);
			ChromaBasedTranscription ct = list.get(0);
			Melody m = new Melody(ct.getNotes(), inputFiles[0]);

			//update stream
			List<Note> notes = m.getNotes();
			for(Note n : notes) {
				stream.addNote(n);
				System.out.println("TRANSCRIBED: " + n.getName());
			}
			
			//get tagging for stream
			FileBasedMelodyDB db = new FileBasedMelodyDB(settings.getDbFile());
			AcceptablePath p = MCSBasedScoreFollower.tag(stream, db.getMelodies());
			System.out.println(p);
		}
	}
}
