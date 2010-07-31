package api.jfugue;

import org.jfugue.Pattern;
import org.jfugue.Player;

import behavior.transcriber.base.Melody;

import java.io.*;
import javax.sound.midi.InvalidMidiDataException;

public class JFugueUtil {
	
	public static void play(Melody m) {
		String jfStr = MelodyToJFugue.convert_pitch_melody(m);
		JFugueUtil.playString(jfStr);
	}
	
	public static void playString(String str) {
		Player player = new Player();
		player.play(str);
	}
	
	public static void play(String midiFile) {
		Player player = new Player();
		try {
			player.playMidiDirectly(new File(midiFile));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidMidiDataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static String loadMusicString(String midiFile) {
		Player player = new Player();
		Pattern p=null;
		try {
			p = player.loadMidi(new File(midiFile));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidMidiDataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return p.getMusicString();
	}
	
	public static void saveMusicString(String midiFile, String musicString) {
		Player player = new Player();
		Pattern p = new Pattern();
		p.add(musicString);
		try {
			player.saveMidi(p, new File(midiFile));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
