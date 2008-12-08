package game;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.io.*;
import schemas.*;
import schema_output.*;
import gui.*;

/**
 * The controller of the  story line in the 
 * text adventure.
 * @author Prateek Tandon
 *
 */
public class StoryMaster extends BrainSchema {
	
	/**
	 * Mapping of file names to story elements
	 */
	private Map<String, StoryElement> fileToStoryElement;

	/**
	 * The current story file (the current state in the story)
	 */
	private String currentStoryFile;
	
	/**
	 * Input from Estimated Mental State. Serves as
	 * the callback for updating the GUI.
	 */
	private Queue<EstimatedMentalStateOutput> emInput;

	/**
	 * The GUI for the game
	 */
	private StoryGUI myGUI;
	
	/**
	 * The MSI gui in existence
	 */
	private GUI msiGUI;
	
	/**
	 * Conenction to Estimated Mental State
	 */
	private EstimatedMentalState em;
	
	/**
	 * Constructor
	 * @param dir The  directory to draw story elements from
	 * @param msiGUI The MSI GUI
	 */
	public StoryMaster(String dir, GUI msiGUI) {
		super("Story Master");
		emInput = new ConcurrentLinkedQueue<EstimatedMentalStateOutput>();
		this.msiGUI  = msiGUI;
		
		//init
		fileToStoryElement = new HashMap<String, StoryElement>();

		//parse story elements
		File dirFile = new File(dir);
		if(dirFile.isDirectory()) {
			File[] files = dirFile.listFiles();

			for(File file : files) {
				StoryElement s = new StoryElement(file.getPath());
				fileToStoryElement.put(file.getName(), s);
			}
		}
		
		//init story gui
		currentStoryFile = "scene1.txt";
		String story = getCurrentStory();
		List<String> actions = getCurrentActions();		
		myGUI = new StoryGUI(msiGUI, story, actions);
		myGUI.setStoryMaster(this);
	}
	
	/**
	 * Used by estimated mental state schema to send input
	 * to this schema.
	 * @param input
	 */
	public void sendEMInput(EstimatedMentalStateOutput input) {
		emInput.add(input);
		receivedInput();
	}

	/**
	 * Updates the story line given the user
	 * executed the given action.
	 * 
	 * @param action The action the user executed.
	 */
	public void progress(String action) {
		StoryElement storyElem = fileToStoryElement.get(currentStoryFile);
		System.out.println(action);
		//execute action on GUI
		String MSI = storyElem.getActionsToMSI().get(action);
		System.out.println("MSI: " + MSI);

		//go to next state
		String nextState = storyElem.getActionsToNextState().get(action);
		currentStoryFile = nextState;
	}
	
	/**
	 * @return  Current Actions
	 */
	public List<String> getCurrentActions() {
		StoryElement storyElem = fileToStoryElement.get(currentStoryFile);
		return storyElem.getActions();
	}
	
	/**
	 * 
	 * @return  Current Story
	 */
	public String getCurrentStory() {
		StoryElement storyElem = fileToStoryElement.get(currentStoryFile);
		return storyElem.getStory();
	}
	
	/**
	 * 
	 * @return Current Story Element
	 */
	public StoryElement getCurrentStoryElement() {
		StoryElement storyElem = fileToStoryElement.get(currentStoryFile);
		return storyElem;		
	}
	
	/**
	 * Produces necessary outputs based on input
	 */
	public boolean produceOutput() {
		if(emInput.size() > 0) {

			//get action executed by user 
			//found by MSI
			EstimatedMentalStateOutput input = emInput.remove();
			String gameSideMSIHash = "";
			if(input.getEstimatedMentalState().equalsIgnoreCase("Nailing Forward Model")) {
				gameSideMSIHash = "nailing";
			}
			else if(input.getEstimatedMentalState().equalsIgnoreCase("Prying Forward Model")) {
				gameSideMSIHash = "prying";
				
			}
			else if(input.getEstimatedMentalState().equalsIgnoreCase("Holding Forward Model")) {
				gameSideMSIHash = "holding";
				
			}
			StoryElement elem = getCurrentStoryElement();
			String action = elem.getMSIToActions().get(gameSideMSIHash);
				
			//update myself with new story element
			progress(action);
			
			//call updates to GUI
			updateGUI();

			return true;
		}
		return false;
	}
	
	/**
	 * Updates the StoryGUI
	 */
	public void updateGUI() {
		StoryElement elemNew = getCurrentStoryElement();
		myGUI.setStory(elemNew.getStory());
		myGUI.setActions(elemNew.getActions());
		myGUI.setButtonsEnabled(true);
	}
	
	/**
	 * Connection to Estimated Mental State
	 * @param em Estimated Mental State  Schema Connection
	 */
	public void setEstimatedMentalState(EstimatedMentalState em) {
		this.em = em;
	}
	
	/**
	 * 
	 * @return ESM
	 */
	public EstimatedMentalState getEstimatedMentalStateSchema() {
		return em;
	}
}