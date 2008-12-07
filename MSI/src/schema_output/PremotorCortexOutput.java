package schema_output;

/**
 * The premotor cortex output struct
 * @author Prateek Tandon
 *
 */
public class PremotorCortexOutput {
	
	/**
	 * The distance from the center initially
	 */
	private double distanceFromCenterInit = -1;

	/**
	 * The angular orientation initially
	 */
	private double orientationAngleDifferenceInit = -1;

	/**
	 * The motor command to execute
	 */
	private String motorCommand = null;
	
	/**
	 * Constructor
	 * @param distanceFromCenterInit
	 * @param orientationAngleDifferenceInit
	 */
	public PremotorCortexOutput(double distanceFromCenterInit, double orientationAngleDifferenceInit) {
		this.distanceFromCenterInit = distanceFromCenterInit;
		this.orientationAngleDifferenceInit = orientationAngleDifferenceInit;
	}
	
	/**
	 * Constructor
	 * @param motorCommand
	 */
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
