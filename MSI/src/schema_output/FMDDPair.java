package schema_output;
import schemas.*;

/**
 * A pair of the forward model name and its 
 * designated discounted difference (for
 * a given time step). Used by difference module.
 * @author Prateek Tandon
 *
 */
public class FMDDPair {
	
	/**
	 * The forward model name
	 */
	private String forwardModel;

	/**
	 * The discounted difference associated with the
	 * forward model
	 */
	private double discountedDifference;
	
	/**
	 * Constructor
	 * @param forwardModel The forward model name
	 * @param discountedDifference The discoutned difference for the forward model
	 */
	public FMDDPair(String forwardModel, double discountedDifference) {
		this.forwardModel = forwardModel;
		this.discountedDifference = discountedDifference;
	}

	/**
	 * @return the forwardModel
	 */
	public String getForwardModel() {
		return forwardModel;
	}

	/**
	 * @param forwardModel the forwardModel to set
	 */
	public void setForwardModel(String forwardModel) {
		this.forwardModel = forwardModel;
	}

	/**
	 * @return the discountedDifference
	 */
	public double getDiscountedDifference() {
		return discountedDifference;
	}

	/**
	 * @param discountedDifference the discountedDifference to set
	 */
	public void setDiscountedDifference(double discountedDifference) {
		this.discountedDifference = discountedDifference;
	}
}
