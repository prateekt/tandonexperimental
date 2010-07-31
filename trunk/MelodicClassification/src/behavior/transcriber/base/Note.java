package behavior.transcriber.base;
import java.io.Serializable;
import java.util.*;
import util.math.dist.Gaussian;

public class Note implements Serializable {
	
	private String name;
	private int duration;
	private List<Double> intensity;
	private double maxIntensity;
	private double averageIntensity;
	private double intensityStdDev;
	
	public Note(String name, int duration, List<Double> intensity) {
		this.name = name;
		this.duration = duration;
		this.intensity = intensity;
		if(intensity!=null) {
			this.maxIntensity = computeMaxIntensity();		
			Gaussian g = new Gaussian(intensity);
			this.averageIntensity = g.computeMean();
			this.intensityStdDev = g.computeStandardDeviation();
		}
	}
	
	@Override
	public String toString() {
		return name + " " + duration + " " + maxIntensity + " " + averageIntensity;
	}

		
	private double computeMaxIntensity() {
		double max = Double.NEGATIVE_INFINITY;
		for(Double d : intensity) {
			if(d > max)
				max = d;
		}
		
		return max;
	}

	/**
	 * @return the intensity
	 */
	public List<Double> getIntensity() {
		return intensity;
	}

	/**
	 * @param intensity the intensity to set
	 */
	public void setIntensity(List<Double> intensity) {
		this.intensity = intensity;
	}

	/**
	 * @return the maxIntensity
	 */
	public double getMaxIntensity() {
		return maxIntensity;
	}

	/**
	 * @param maxIntensity the maxIntensity to set
	 */
	public void setMaxIntensity(double maxIntensity) {
		this.maxIntensity = maxIntensity;
	}

	/**
	 * @return the averageIntensity
	 */
	public double getAverageIntensity() {
		return averageIntensity;
	}

	/**
	 * @param averageIntensity the averageIntensity to set
	 */
	public void setAverageIntensity(double averageIntensity) {
		this.averageIntensity = averageIntensity;
	}

	/**
	 * @return the intensityStdDev
	 */
	public double getIntensityStdDev() {
		return intensityStdDev;
	}

	/**
	 * @param intensityStdDev the intensityStdDev to set
	 */
	public void setIntensityStdDev(double intensityStdDev) {
		this.intensityStdDev = intensityStdDev;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the duration
	 */
	public int getDuration() {
		return duration;
	}

	/**
	 * @param duration the duration to set
	 */
	public void setDuration(int duration) {
		this.duration = duration;
	}
}
