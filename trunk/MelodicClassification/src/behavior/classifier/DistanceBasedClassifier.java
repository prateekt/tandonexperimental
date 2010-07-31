package behavior.classifier;

import java.util.ArrayList;
import java.util.List;

import behavior.transcriber.base.Melody;

import util.math.StringMath;
import weka.classifiers.Classifier;
import weka.core.Instances;

public class DistanceBasedClassifier extends Classifier {
	
	protected List<Melody> melodies;

	public String classify(Melody m) {
		int min_distance = Integer.MAX_VALUE;
		Melody closest = null;
		for(int x=0; x < melodies.size(); x++) {
			Melody current = melodies.get(x);

			//convert to note mask
			String mine = StringMath.toNoteMask(m.getNotes());
			String other = StringMath.toNoteMask(current.getNotes());
			
			//absolute lev distance
			int dist = Math.abs(StringMath.LevenshteinDistance(mine, other));
			
			if(dist < min_distance) {
				min_distance = dist;
				closest = current;
			}
		}
		
		if(closest==null)
			return null;
		
		return closest.getName();
	}
	
	public DistanceBasedClassifier() {
		melodies = new ArrayList<Melody>();
	}
	
	public DistanceBasedClassifier(List<Melody> melodies) {
		this.melodies = melodies;
	}
		
	public void addMelody(Melody m) {
		melodies.add(m);
		System.out.println("ADDED: " +m);
	}
	
	/**
	 * @return the melodies
	 */
	public List<Melody> getMelodies() {
		return melodies;
	}

	/**
	 * @param melodies the melodies to set
	 */
	public void setMelodies(List<Melody> melodies) {
		this.melodies = melodies;
	}

	//unused WEKA Method
	@Override
	public void buildClassifier(Instances arg0) throws Exception {}
}
