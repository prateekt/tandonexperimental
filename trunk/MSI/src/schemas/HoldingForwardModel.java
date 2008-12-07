package schemas;

/**
 * Forward model for hammer holding hypothesis.
 * @author Prateek Tandon
 *
 */
public class HoldingForwardModel extends ForwardModel {
	
	/**
	 * Constructor
	 */
	public HoldingForwardModel() {
		super("Holding Forward Model");
		finalDistanceFromCenter = 300.0;
		finalOrientationDifference = Math.PI / 2;
	}
}
