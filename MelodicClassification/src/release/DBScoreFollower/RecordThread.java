package release.DBScoreFollower;

import java.util.concurrent.BlockingQueue;

import release.DBRecognizer.DBRecognizerGUI;

import api.soundrecoder.SoundRecorderUtil;

public class RecordThread implements Runnable {
	
	private BlockingQueue<String> queue;
	private int ctr=0;
	
	public RecordThread(BlockingQueue<String> queue) {
		this.queue = queue;
	}
	
	public void run() {
		
		while(true) {
		
			//record
			String fileName = "test"+ctr+".wav";
			recordRoutine(fileName, DBRecognizerGUI.RECORD_DIR);
	
			//add to queue
			try {
				queue.put(fileName);
			}
			catch(Exception e) {
				e.printStackTrace();
			}
			
			ctr++;
		}
	}
	
	public void recordRoutine(String fileName, String dir) {
		SoundRecorderUtil.record(dir + "/" + fileName, DBRecognizerGUI.RECORD_TIME);
	}				
}
