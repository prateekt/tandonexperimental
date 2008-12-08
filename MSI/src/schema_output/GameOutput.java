package schema_output;
import game.*;

/**
 * Models the output of the game to the Estimated Mental State
 * Schema
 * @author Prateek Tandon
 *
 */
public class GameOutput {
	
	/**
	 * The story master that sent the output.
	 */
	private StoryMaster sm;
	
	/**
	 * The action taken in the story right now.
	 */
	private String msiAction;
	
	/**
	 * Constructor
	 * @param sm Story Master
	 * @param msiAction MSI Mapped Action
	 */
	public GameOutput(StoryMaster sm, String msiAction) {
		this.sm = sm;
		this.msiAction = msiAction;
	}

	/**
	 * @return the sm
	 */
	public StoryMaster getSm() {
		return sm;
	}

	/**
	 * @param sm the sm to set
	 */
	public void setSm(StoryMaster sm) {
		this.sm = sm;
	}

	/**
	 * @return the msiAction
	 */
	public String getMsiAction() {
		return msiAction;
	}

	/**
	 * @param msiAction the msiAction to set
	 */
	public void setMsiAction(String msiAction) {
		this.msiAction = msiAction;
	}
}
