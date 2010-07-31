package release.DBRecognizer;

import java.util.List;
import java.util.concurrent.BlockingQueue;

import behavior.transcriber.base.Melody;
import behavior.transcriber.chroma_based.ChromaBasedTranscription;
import behavior.transcriber.chroma_based.ChromaNoteTranscriber;


public class RecognizerThread implements Runnable {

	private BlockingQueue<String> queue;
	private DBRecognizerGUI gui;
	
	public RecognizerThread(BlockingQueue<String> queue, DBRecognizerGUI gui) {
		this.queue = queue;
		this.gui = gui;
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
			gui.getStatusLabel().setText("Transcribing Notes...");
			List<ChromaBasedTranscription> list = ChromaNoteTranscriber.transcribe(DBRecognizerGUI.RECORD_DIR+"/", inputFiles, DBRecognizerGUI.CHROMA_CLASSIFY_MATLAB_FILE);
			ChromaBasedTranscription ct = list.get(0);
			Melody m = new Melody(ct.getNotes(), inputFiles[0]);
			String label = gui.getClassifier().classify(m);
			if(label==null)
				label = "NONE_FOUND";
			gui.getLastRecField().setText("Last: " + label);
			gui.getStatusLabel().setText("Done transcribing notes!");
			
			//add new rec event
			Time t= gui.getGlobalTime().addSeconds(DBRecognizerGUI.RECORD_TIME);
			RecognitionEvent r = new RecognitionEvent(label,t,m.compareStr());
			gui.getEvents().add(r);
			
			//update table
			gui.getTableDialog().updateTable(gui.getEvents());
		}
	}

}
