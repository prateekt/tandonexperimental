package game;

import java.io.*;
import java.util.*;

/**
 * Represents a particular state in the story. Story states
 * and transition conditions are stored in scene files in the
 * scenes directory.
 * @author Prateek Tandon
 *
 */
public class StoryElement {
	
	/**
	 * The story line associated with this state.
	 */
	private String story;
	
	/**
	 * The actions that lead to next states.
	 */
	private List<String> actions;
	
	/**
	 * Actions mapped to MSI actions that can be
	 * detected by a web cam. Actions role played in front
	 * of the camera are related by this to actions in the 
	 * story.
	 */
	private Map<String, String> actionsToMSI;
	
	/**
	 * The inverse function of the above.
	 */
	private Map<String, String> MSIToActions;

	/**
	 * Actions from this state mapped to next state.
	 */
	private Map<String, String> actionsToNextState;
	
	/**
	 * Story element object
	 * @param file The file that creates this story element object.
	 */
	public StoryElement(String file) {
		//init
		actions = new ArrayList<String>();
		actionsToMSI = new HashMap<String,String>();
		actionsToNextState = new HashMap<String, String>();
		MSIToActions = new HashMap<String, String>();

		//parse file
		try {
			BufferedReader r = new BufferedReader(new FileReader(file));
			String line="";
			int lineNum=0;
			while((line=r.readLine())!=null) {
				if(lineNum==0) {
					story = line.trim();
				}
				if(lineNum==1) {
					String[] actionsArr = line.split(",");
					for(String action : actionsArr) {
						actions.add(action.trim());
					}
				}
				if(lineNum==2) {
					String[] MSIMap = line.split(",");
					for(int x=0; x < actions.size(); x++) {
						actionsToMSI.put(actions.get(x), MSIMap[x].trim());
						MSIToActions.put(MSIMap[x].trim(), actions.get(x));
					}
				}
				if(lineNum==3) {
					String[] trans = line.split(",");
					for(int x=0; x < actions.size(); x++) {
						actionsToNextState.put(actions.get(x), trans[x].trim());
					}
				}

				lineNum++;
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @return the story
	 */
	public String getStory() {
		return story;
	}


	/**
	 * @return the actions
	 */
	public List<String> getActions() {
		return actions;
	}


	/**
	 * @return the actionsToMSI
	 */
	public Map<String, String> getActionsToMSI() {
		return actionsToMSI;
	}


	/**
	 * @return the actionsToNextState
	 */
	public Map<String, String> getActionsToNextState() {
		return actionsToNextState;
	}


	/**
	 * @return the mSIToActions
	 */
	public Map<String, String> getMSIToActions() {
		return MSIToActions;
	}
}