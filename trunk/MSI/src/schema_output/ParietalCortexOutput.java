package schema_output;

import util.Vector2;

public class ParietalCortexOutput {
	private Vector2 knuckleVector;
	private Vector2 hammerHandleVector;
	private double orientationAngleDifference;
	private double distanceFromCenter;
	
	public ParietalCortexOutput(double distanceFromCenter, double orientationAngleDifference) {
		this.orientationAngleDifference = orientationAngleDifference;
		this.distanceFromCenter = distanceFromCenter;
	}

	/**
	 * @return the knuckleVector
	 */
	public Vector2 getKnuckleVector() {
		return knuckleVector;
	}

	/**
	 * @param knuckleVector the knuckleVector to set
	 */
	public void setKnuckleVector(Vector2 knuckleVector) {
		this.knuckleVector = knuckleVector;
	}

	/**
	 * @return the hammerHandleVector
	 */
	public Vector2 getHammerHandleVector() {
		return hammerHandleVector;
	}

	/**
	 * @param hammerHandleVector the hammerHandleVector to set
	 */
	public void setHammerHandleVector(Vector2 hammerHandleVector) {
		this.hammerHandleVector = hammerHandleVector;
	}

	/**
	 * @return the orientationAngleDifference
	 */
	public double getOrientationAngleDifference() {
		return orientationAngleDifference;
	}

	/**
	 * @param orientationAngleDifference the orientationAngleDifference to set
	 */
	public void setOrientationAngleDifference(double orientationAngleDifference) {
		this.orientationAngleDifference = orientationAngleDifference;
	}
	
	/**
	 * @return the distanceFromCenter
	 */
	public double getDistanceFromCenter() {
		return distanceFromCenter;
	}

	/**
	 * @param distanceFromCenter the distanceFromCenter to set
	 */
	public void setDistanceFromCenter(double distanceFromCenter) {
		this.distanceFromCenter = distanceFromCenter;
	}

	public static ParietalCortexOutput randForDebug() {
		return new ParietalCortexOutput(Math.random()*3, Math.random()*3);	
	}
}
