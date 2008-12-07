package schemas;

/**
 * Forwad model for prying hypothesis.
 * @author Prateek Tandon
 *
 */
public class PryingForwardModel extends ForwardModel {
	
	/**
	 * Constructor
	 */
	public PryingForwardModel() {
		super("Prying Forward Model");
		finalDistanceFromCenter = 0.0;
		finalOrientationDifference = Math.PI;
	}
	
}
