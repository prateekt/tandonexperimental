package util.math.dist;
import java.util.*;

public class GaussianNoiseGenerator {
	
	public static List<Integer> genNoisePoints(int points, int center) {
		List<Integer> rtn = new ArrayList<Integer>();
		
		for(int x=0; x < points; x++) {
			double d = center*Math.random();
			rtn.add((int)d);
		}
		
//		Gaussian g = new Gaussian(0,10);
/*		for(int x=0; x < points; x++) {
			double d = g.computeGaussian(3*Math.random());
			rtn.add(d);
		}*/
		
		return rtn;
	}
}
