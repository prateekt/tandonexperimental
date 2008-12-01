package schema_output;
import java.util.*;

public class DifferenceModuleOutput {
	
	private List<FMDDPair> pairs;
	
	public DifferenceModuleOutput(List<FMDDPair> pairs) {
		this.pairs = pairs;
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
}
