package schema_output;
import schemas.*;

public class FMDDPair {
	
	private ForwardModel forwardModel;
	private double discountedDifference;
	
	public FMDDPair(ForwardModel forwardModel, double discountedDifference) {
		this.forwardModel = forwardModel;
		this.discountedDifference = discountedDifference;
	}

	/**
	 * @return the forwardModel
	 */
	public ForwardModel getForwardModel() {
		return forwardModel;
	}

	/**
	 * @param forwardModel the forwardModel to set
	 */
	public void setForwardModel(ForwardModel forwardModel) {
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
