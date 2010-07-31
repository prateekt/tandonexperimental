package release.DBScoreFollower;
import java.util.*;
import java.util.concurrent.*;

import release.DBLoader.DefaultUserSetting;
import release.DBLoader.UserSetting;
public class ScoreFollower {
	
	public static void main(String[] args) {
	
		BlockingQueue<String> queue = new LinkedBlockingQueue<String>();
		UserSetting us = new DefaultUserSetting();
		
		RecognizerThread recogT = new RecognizerThread(queue,us);
		RecordThread recoT = new RecordThread(queue);
		
		//start threads
		Thread t1 = new Thread(recogT);
		Thread t2 = new Thread(recoT);
		t1.start();
		t2.start();
	}
	
}
