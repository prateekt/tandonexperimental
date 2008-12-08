package game;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import gui.*;
import schema_output.*;

/**
 * The GUI for the game.
 * @author Prateek Tandon
 */
public class StoryGUI extends JFrame implements ActionListener {
	
	/**
	 * Buttons on gui that allow user to execute actions.
	 */
	private JButton b1, b2, b3;
	
	/**
	 * The text area where the story is displayed.
	 */
	private JTextArea storyArea;

	/**
	 * The MSI GUI object.
	 */
	private GUI msiGUI;

	/**
	 * The Story master that  controls the story
	 * and the owner for this gui.
	 */
	private StoryMaster owner;
	
	/**
	 * Constructor
	 * @param msiGUI MSI GUI in existence
	 * @param initialStory The initial story element
	 * @param initialActions The initial story actions
	 */
	public StoryGUI(GUI msiGUI, String initialStory, java.util.List<String> initialActions) {
		super("Role-Playing Text Adventure (Prateek Tandon)");
		this.msiGUI = msiGUI;
		
		//init
		b1 = new JButton();
		b2 = new JButton();
		b3 = new JButton();
		storyArea = new JTextArea(50,70);
		storyArea.setEditable(false);
		storyArea.setLineWrap(true);
		storyArea.setWrapStyleWord(true);
		JScrollPane scrollPane = new JScrollPane(storyArea);
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(null);
		buttonPanel.add(b1);
		buttonPanel.add(b2);
		buttonPanel.add(b3);
		b1.setBounds(0,10,170,25);
		b2.setBounds(170,10,170,25);
		b3.setBounds(340,10,170,25);
		b1.setFont(new Font("Times New Roman", Font.PLAIN, 10));
		b2.setFont(new Font("Times New Roman", Font.PLAIN, 10));
		b3.setFont(new Font("Times New Roman", Font.PLAIN, 10));
		b1.addActionListener(this);
		b2.addActionListener(this);
		b3.addActionListener(this);
		getContentPane().setLayout(null);		
		getContentPane().add(scrollPane);
		getContentPane().add(buttonPanel);
		
		//layout
		scrollPane.setBounds(10,10,490, 200);
		buttonPanel.setBounds(10,210, 490, 100);

		//story elems
		setActions(initialActions);
		setStory(initialStory);
		
		//show
		this.setSize(520,280);
		this.setResizable(false);
		this.setVisible(true);
	}
	
	/**
	 * Sets the actions on the gui buttons.
	 * @param actions The actions to set
	 */
	public void setActions(java.util.List<String> actions)  {
		b1.setText(actions.get(0));
		b2.setText(actions.get(1));
		b3.setText(actions.get(2));
	}

	/**
	 * Whether to enable the buttons or not.
	 * @param on Enable or not
	 */
	public void setButtonsEnabled(boolean on) {
		b1.setEnabled(on);
		b2.setEnabled(on);
		b3.setEnabled(on);
	}
		
	/**
	 * The story to set
	 * @param story story
	 */
	public void setStory(String story) {
		storyArea.setText(story);
	}
	
	/**
	 * When a button is clicked, simulate motion
	 * using webcam and then wait for callback
	 * so that gui can be updated with inferred mental
	 * state from web cam.
	 */
	public void actionPerformed(ActionEvent e) {

		//simulate necessary action event on MSI GUI (a hack for now)
		String label = e.getActionCommand();
		String msiAction = owner.getCurrentStoryElement().getActionsToMSI().get(label);
		
		
		//handle reset case
		if(msiAction.equalsIgnoreCase("reset")) {
			
			//just tell story master to reset
			//and tell him to update me
			owner.progress(msiAction);
			owner.updateGUI();
			return;
		}
		
		
		//normal case
		JButton source = null;
		if(msiAction.equalsIgnoreCase("nailing")) {
			source = msiGUI.getButtonPanel().getNailingButton();
		}
		else if(msiAction.equalsIgnoreCase("prying")) {
			source = msiGUI.getButtonPanel().getPryingButton();
			
		}
		else {
			source = msiGUI.getButtonPanel().getHoldingButton();			
		}			
		msiGUI.getButtonPanel().actionPerformed(new ActionEvent(source, 2, source.getLabel()));
				
		//send context to ESM so knows that story master is here
		owner.getEstimatedMentalStateSchema().sendGameInput(new GameOutput(owner, msiAction));
		
		//display buttons while we wait for result
		b1.setEnabled(false);
		b2.setEnabled(false);
		b3.setEnabled(false);
	}	
	
	/**
	 * Sets the master
	 * @param owner the master
	 */
	public void setStoryMaster(StoryMaster owner) {
		this.owner = owner;
	}
}