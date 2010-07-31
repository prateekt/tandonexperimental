package behavior.classifier;

import util.math.StringMath;
import util.math.MelodySegmentation.MelodyMath;

import java.util.*;

import behavior.transcriber.base.Melody;


public class ScaleInvariantClassifier extends DistanceBasedClassifier {
	
	@Override
	public void addMelody(Melody m) {
		List<Melody> toAdd = MelodyMath.generateAllTranspositions(m);
		for(Melody m2 : toAdd) {
			melodies.add(m2);
			System.out.println("ADDED: " +m2);
		}
	}
		
	public String classify_scaleInvariant_algo(Melody m) {

		//convert to note mask and compute hash
		String mine = StringMath.toNoteMask(m.getNotes());
		String my_hash = StringMath.getDistanceHash(mine);
		
		int min_distance = Integer.MAX_VALUE;
		Melody closest = null;
		for(int x=0; x < melodies.size(); x++) {
			Melody current = melodies.get(x);
			
			//convert to note mask and compute hash
			String other = StringMath.toNoteMask(current.getNotes());
			String other_hash = StringMath.getDistanceHash(other);
			int dist = StringMath.compareDistHashes(my_hash, other_hash);
			
			if(dist < min_distance) {
				min_distance = dist;
				closest = current;
			}			
		}
		
		if(closest==null)
			return null;
		
		return closest.getName();
	}
}
