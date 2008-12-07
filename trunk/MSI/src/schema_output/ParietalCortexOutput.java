package schema_output;

import util.Vector2;

/**
 * Struct for parietal cortex output
 * @author Prateek Tandon
 *
 */
public class ParietalCortexOutput {
	
	/**
	 * The knuckle vector at this time step
	 */
	private Vector2 knuckleVector;

	/**
	 * The hammer handle vector at this time step
	 */
	private Vector2 hammerHandleVector;

	/**
	 * The orientation angle difference between the knuckle vector
	 * and the hammer handle vector for this time step.
	 */
	private double orientationAngleDifference;

	/**
	 * The distance of the hand from the center of the hammer
	 * for this time step.
	 */
	private double distanceFromCenter;
	
	/**
	 * Constructor
	 * @param distanceFromCenter DOC
	 * @param orientationAngleDifference  OA
	 */
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
	
	/**
	 * Generates a random output for debugging purposes
	 * @return random output
	 */
	public static ParietalCortexOutput randForDebug() {
		return new ParietalCortexOutput(Math.random()*3, Math.random()*3);	
	}
}
