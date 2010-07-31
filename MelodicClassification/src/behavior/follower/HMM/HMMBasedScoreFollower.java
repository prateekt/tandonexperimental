package behavior.follower.HMM;

import java.util.*;

import behavior.transcriber.base.*;
import api.tuxguitar.MelodyExtractor.TGMelodyExtractor;

public class HMMBasedScoreFollower {
	
	public static void main(String[] args) {
		Map<String, List<Melody>> melodies = TGMelodyExtractor.extractMelodies("db/db_gtp/jm.gp4",0,0,0);
		String it = "";
		for(String key : melodies.keySet()) {
			it = key;
		}
		
		List<Melody> track0Melodies = melodies.get(it);
		
		HMMImpl i = HMMEncoder.encode(track0Melodies);
		
		List<Note> m0Notes = track0Melodies.get(1).getNotes();
		List<String> obs = new ArrayList<String>();
		for(Note n : m0Notes) {
			obs.add(n.getName());
		}
				
		for(String currentObs : obs) {
			Melody hypo1 = i.computeL(obs, currentObs);
			Melody hypo2 = i.computeL_viterbi(obs, currentObs);
			System.out.println(currentObs + " " + hypo1.getName() + " " + hypo2.getName());
		}
	}
}
