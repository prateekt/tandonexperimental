package schema_output;
import java.util.*;

/**
 * The difference module output struct.
 * @author Prateek Tandon
 *
 */
public class DifferenceModuleOutput {
	
	/**
	 * List of forward model and discounted difference pairs.
	 */
	private List<FMDDPair> pairs;

	/**
	 * The current time step.
	 */
	private int timeStep=-1;
	
	/**
	 * Contructor
	 * @param pairs
	 * @param timeStep
	 */
	public DifferenceModuleOutput(List<FMDDPair> pairs, int timeStep) {
		this.pairs = pairs;
		this.timeStep = timeStep;
	}

	/**
	 * @return the pairs
	 */
	public List<FMDDPair> getPairs() {
		return pairs;
	}

	/**
	 * @param pairs the pairs to set
	 */
	public void setPairs(List<FMDDPair> pairs) {
		this.pairs = pairs;
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
