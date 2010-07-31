package api.soundrecoder;
// Originally code from jsresources.org

// Was buggy and fixed by:
// http://www.redbrick.dcu.ie/~jr/audio/

// Then modified by Bert Szoghy, webmaster@quadmore.com

import java.io.IOException;
import java.io.File;
import javax.sound.sampled.TargetDataLine;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioFileFormat;

public class SoundRecorder extends Thread
{
	private TargetDataLine	m_line;
	private AudioFileFormat.Type m_targetType;
	private AudioInputStream m_audioInputStream;
	private File m_outputFile;

	/**
	 * @return the m_line
	 */
	public TargetDataLine getM_line() {
		return m_line;
	}

	/**
	 * @param mLine the m_line to set
	 */
	public void setM_line(TargetDataLine mLine) {
		m_line = mLine;
	}

	/**
	 * @return the m_targetType
	 */
	public AudioFileFormat.Type getM_targetType() {
		return m_targetType;
	}

	/**
	 * @param mTargetType the m_targetType to set
	 */
	public void setM_targetType(AudioFileFormat.Type mTargetType) {
		m_targetType = mTargetType;
	}

	/**
	 * @return the m_audioInputStream
	 */
	public AudioInputStream getM_audioInputStream() {
		return m_audioInputStream;
	}

	/**
	 * @param mAudioInputStream the m_audioInputStream to set
	 */
	public void setM_audioInputStream(AudioInputStream mAudioInputStream) {
		m_audioInputStream = mAudioInputStream;
	}

	/**
	 * @return the m_outputFile
	 */
	public File getM_outputFile() {
		return m_outputFile;
	}

	/**
	 * @param mOutputFile the m_outputFile to set
	 */
	public void setM_outputFile(File mOutputFile) {
		m_outputFile = mOutputFile;
	}

	public SoundRecorder(TargetDataLine line,
				     AudioFileFormat.Type targetType,
				     File file)
	{
		m_line = line;
		m_audioInputStream = new AudioInputStream(line);
		m_targetType = targetType;
		m_outputFile = file;
	}

	/** Starts the recording.
	    To accomplish this, (i) the line is started and (ii) the
	    thread is started.
	*/
	@Override
	public void start()
	{
		/* Starting the TargetDataLine. It tells the line that
		   we now want to read data from it. If this method
		   isn't called, we won't
		   be able to read data from the line at all.
		*/
		m_line.start();

		/* Starting the thread. This call results in the
		   method 'run()' (see below) being called. There, the
		   data is actually read from the line.
		*/
		super.start();
	}

	/** Stops the recording.
	*/
	public void stopRecording()
	{
		m_line.stop();
		m_line.close();
	}

	/** Main working method.
	*/
	@Override
	public void run()
	{
			try
			{
				AudioSystem.write(
					m_audioInputStream,
					m_targetType,
					m_outputFile);
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
	}

	private static void closeProgram()
	{
		System.out.println("Program closing.....");
		System.exit(1);
	}

	private static void out(String strMessage)
	{
		System.out.println(strMessage);
	}

	public static void main(String[] args)
	{

	}
}
