package schema_output;
import schemas.*;
import java.util.*;

/**
 * Estimated Mental State Output Struct
 * @author Prateek Tandon
 *
 */
public class EstimatedMentalStateOutput {
	
	/**
	 * Forward model name with lowest discounted difference.
	 */
	private String estimatedMentalState;
	
	/**
	 * Mapping of forward models to probability values
	 * of that forward model.
	 */
	private Map<String,Double> fmToProb;
	
	/**
	 * Current Time Step
	 */
	private int timeStep = 0;
	
	/**
	 * Contructor
	 * @param estimatedMentalState The estimated mental state at this time step
	 * @param fmToProb A map from forward model to its probability of being the action the user is executing
	 * @param timeStep The current time step
	 */
	public EstimatedMentalStateOutput(String estimatedMentalState, Map<String, Double> fmToProb, int timeStep) {
		this.estimatedMentalState = estimatedMentalState;
		this.fmToProb = fmToProb;
		this.timeStep = timeStep;
	}

	/**
	 * @return the estimatedMentalState
	 */
	public String getEstimatedMentalState() {
		return estimatedMentalState;
	}

	/**
	 * @param estimatedMentalState the estimatedMentalState to set
	 */
	public void setEstimatedMentalState(String estimatedMentalState) {
		this.estimatedMentalState = estimatedMentalState;
	}
		
	/**
	 * @return the fmToProb
	 */
	public Map<String, Double> getFmToProb() {
		return fmToProb;
	}

	/**
	 * @param fmToProb the fmToProb to set
	 */
	public void setFmToProb(Map<String, Double> fmToProb) {
		this.fmToProb = fmToProb;
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

	public String toString() {
		return estimatedMentalState;
	}
}
