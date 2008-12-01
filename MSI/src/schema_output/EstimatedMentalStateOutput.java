package schema_output;
import schemas.*;
import java.util.*;

public class EstimatedMentalStateOutput {
	
	/**
	 * Forward model with lowest discounted difference.
	 */
	private ForwardModel estimatedMentalState;
	
	/**
	 * Mapping of forward models to probability values
	 * of that forward model.
	 */
	private Map<ForwardModel,Double> fmToProb;
	
	public EstimatedMentalStateOutput(ForwardModel estimatedMentalState, Map<ForwardModel, Double> fmToProb) {
		this.estimatedMentalState = estimatedMentalState;
		this.fmToProb = fmToProb;
	}

	/**
	 * @return the estimatedMentalState
	 */
	public ForwardModel getEstimatedMentalState() {
		return estimatedMentalState;
	}

	/**
	 * @param estimatedMentalState the estimatedMentalState to set
	 */
	public void setEstimatedMentalState(ForwardModel estimatedMentalState) {
		this.estimatedMentalState = estimatedMentalState;
	}

	/**
	 * @return the fmToProb
	 */
	public Map<ForwardModel, Double> getFmToProb() {
		return fmToProb;
	}

	/**
	 * @param fmToProb the fmToProb to set
	 */
	public void setFmToProb(Map<ForwardModel, Double> fmToProb) {
		this.fmToProb = fmToProb;
	}
	
	public String toString() {
		return estimatedMentalState.getName();
	}
}
