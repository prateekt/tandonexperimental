package schema_output;

public class PremotorCortexOutput {
	
	private double distanceFromCenterInit = -1;
	private double orientationAngleDifferenceInit = -1;
	private String motorCommand = null;
	
	public PremotorCortexOutput(double distanceFromCenterInit, double orientationAngleDifferenceInit) {
		this.distanceFromCenterInit = distanceFromCenterInit;
		this.orientationAngleDifferenceInit = orientationAngleDifferenceInit;
	}
	
	public PremotorCortexOutput(String motorCommand) {
		this.motorCommand = motorCommand;
	}

	/**
	 * @return the distanceFromCenterInit
	 */
	public double getDistanceFromCenterInit() {
		return distanceFromCenterInit;
	}

	/**
	 * @param distanceFromCenterInit the distanceFromCenterInit to set
	 */
	public void setDistanceFromCenterInit(double distanceFromCenterInit) {
		this.distanceFromCenterInit = distanceFromCenterInit;
	}

	/**
	 * @return the orientationAngleDifferenceInit
	 */
	public double getOrientationAngleDifferenceInit() {
		return orientationAngleDifferenceInit;
	}

	/**
	 * @param orientationAngleDifferenceInit the orientationAngleDifferenceInit to set
	 */
	public void setOrientationAngleDifferenceInit(
			double orientationAngleDifferenceInit) {
		this.orientationAngleDifferenceInit = orientationAngleDifferenceInit;
	}

	/**
	 * @return the motorCommand
	 */
	public String getMotorCommand() {
		return motorCommand;
	}

	/**
	 * @param motorCommand the motorCommand to set
	 */
	public void setMotorCommand(String motorCommand) {
		this.motorCommand = motorCommand;
	}
}
