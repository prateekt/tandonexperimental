package schema_output;
import schemas.*;

public class ControlVariableSummary {
	
	private ForwardModel sender;
	private double distanceFromCenter;
	private double orientationDifference;
	private int timeStep;
	
	public ControlVariableSummary(ForwardModel sender, double distanceFromCenter, double orientationDifference, int timeStep) {
		this.sender = sender;
		this.distanceFromCenter = distanceFromCenter;
		this.orientationDifference = orientationDifference;
		this.timeStep = timeStep;
	}
	
	/**
	 * @return the sender
	 */
	public ForwardModel getSender() {
		return sender;
	}
	/**
	 * @param sender the sender to set
	 */
	public void setSender(ForwardModel sender) {
		this.sender = sender;
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
	 * @return the orientationDifference
	 */
	public double getOrientationDifference() {
		return orientationDifference;
	}
	/**
	 * @param orientationDifference the orientationDifference to set
	 */
	public void setOrientationDifference(double orientationDifference) {
		this.orientationDifference = orientationDifference;
	}

	/**
	 * @return the timeStep
	 */
	public int getTimeStep() {
		return timeStep;
	}

	/**
	 * @param timeStep the timeStep to set
	 */
	public void setTimeStep(int timeStep) {
		this.timeStep = timeStep;
	}
}
