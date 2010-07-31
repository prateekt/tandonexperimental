package api.soundrecoder;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.*;
import javax.sound.sampled.*;

public class SoundRecorder2 {
	protected boolean running;
	ByteArrayOutputStream out;
	
	private AudioFormat getFormat() {
		AudioFormat	audioFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 44100.0F, 16, 2, 4, 44100.0F, false);
		return audioFormat;
	}
	
	public void record(String outputFile, int numSeconds) {
		try {
			final AudioFormat format = getFormat();
			DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
			final TargetDataLine line = (TargetDataLine) AudioSystem.getLine(info);
			line.open(format);
			line.start();
			final byte[] buffer;
			buffer = new byte[(int)format.getSampleRate()* format.getFrameSize()];
			final ByteArrayOutputStream baoStream = new ByteArrayOutputStream();
			
			Runnable runner = new Runnable() {
 
				public void run() {
					running = true;

					while (running) {
						int count = line.read(buffer, 0, buffer.length);
						  if (count > 0) {
							  try {
								  baoStream.write(buffer);
							  }
							  catch(Exception e) {
								  e.printStackTrace();
							  }
						  }
					}
				}
			};
      
			Thread captureThread = new Thread(runner);
			captureThread.start();

			//callback
			try {
				Thread.sleep(1000*numSeconds);
			}
			catch(Exception e) {
				e.printStackTrace();
			}
			running=false;
			
			//Save the recorded sound into a file
	        ByteArrayInputStream baiStream = new ByteArrayInputStream(baoStream.toByteArray());
			File audioFile = new File(outputFile);
			AudioInputStream aiStream = new AudioInputStream(baiStream,getFormat(),buffer.length);
			AudioSystem.write(aiStream,AudioFileFormat.Type.WAVE,audioFile);
			aiStream.close();
			baiStream.close();
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
