package schema_output;

/**
 * Struct for prefrontal cortex output.
 * @author Prateek Tandon
 *
 */
public class PrefrontalCortexOutput {
	
	/**
	 * The task parameter currently.
	 */
	private int taskParameter;
	
	/**
	 * The constructor
	 * @param taskParameter The  task the organism is trying to do
	 */
	public PrefrontalCortexOutput(int taskParameter) {
		this.taskParameter = taskParameter;
	}

	/**
	 * @return the taskParameter
	 */
	public int getTaskParameter() {
		return taskParameter;
	}

	/**
	 * @param taskParameter the taskParameter to set
	 */
	public void setTaskParameter(int taskParameter) {
		this.taskParameter = taskParameter;
	}
}
