package api.soundrecoder;

public class SoundRecorderUtil {
	
	private static SoundRecorder2 recorder=null;	
	
/*	public static void init() {
		AudioFormat	audioFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 44100.0F, 16, 2, 4, 44100.0F, false);

		DataLine.Info	info = new DataLine.Info(TargetDataLine.class, audioFormat);
		TargetDataLine	targetDataLine = null;
		try {
			targetDataLine = (TargetDataLine) AudioSystem.getLine(info);
			targetDataLine.open(audioFormat);
		}
		catch (LineUnavailableException e) {
			System.out.println("unable to get a recording line");
			e.printStackTrace();
			System.exit(1);
		}

		AudioFileFormat.Type targetType = AudioFileFormat.Type.WAVE;

		recorder = new SoundRecorder(targetDataLine,targetType);
	}*/
	
	public static void record(String outputFile, int numSeconds) {
		
		//init if need to
		if(recorder==null) {
			recorder = new SoundRecorder2();
		}
		
		recorder.record(outputFile, numSeconds);
		
/*		//tell recorder recording file
		recorder.setM_outputFile(new File(outputFile));
		
		//Here, the recording is actually started.
		recorder.start();

		//wait
		try {
			Thread.sleep(numSeconds*1000);
		}
		catch(Exception e) {
			e.printStackTrace();
		}

		//Here, the recording is actually stopped.
		recorder.stopRecording();*/
	}
	
	public static void main(String[] args) {
		record("TESTA.wav", 5);
		System.out.println("First done");
		record("TESTB.wav", 5);
		System.out.println("Second done");
	}
	
}
