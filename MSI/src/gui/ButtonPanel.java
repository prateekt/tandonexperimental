package gui;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import util.*;
import webcam.*;

/**
 * Contains Demo Buttons
 * @author Prateek Tandon
 */
public class ButtonPanel extends JPanel implements ActionListener {

	/**
	 * The button for executing the holding demo.
	 */
	private JButton holdingButton;

	/**
	 * The button  for executing the prying demo.
	 */
	private JButton pryingButton;

	/**
	 * The  button for executing the nailing demo.
	 */
	private JButton nailingButton;
	
	/**
	 * The field showing the inferred mental state.
	 */
	private JTextField inferField;

	/**
	 * The owner of this panel.
	 */
	private GUI owner;
	
	/**
	 * Constructor
	 * @param owner The owner of this panel
	 */
	public ButtonPanel(GUI owner) {
		this.owner = owner;
		setLayout(new FlowLayout());
		holdingButton = new JButton("Holding Demo");
		pryingButton = new  JButton("Prying Demo");
		nailingButton = new JButton("Nailing Demo");
		inferField = new JTextField(20);
		inferField.setEditable(false);
		add(nailingButton);
		add(pryingButton);
		add(holdingButton);
		add(inferField);
		holdingButton.addActionListener(this);
		pryingButton.addActionListener(this);
		nailingButton.addActionListener(this);
	}
	
	/**
	 * Callback for running demos
	 */
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==holdingButton) {
			MockWebCam webcam = (MockWebCam) Constants.getWebCam();
			webcam.setImageDir(Constants.HOLDING_DEMO_DIR);
			Constants.restartSchemas();
		}
		if(e.getSource()==pryingButton) {
			MockWebCam webcam = (MockWebCam) Constants.getWebCam();
			webcam.setImageDir(Constants.PRYING_DEMO_DIR);
			Constants.restartSchemas();			
		}
		if(e.getSource()==nailingButton) {
			MockWebCam webcam = (MockWebCam) Constants.getWebCam();
			webcam.setImageDir(Constants.NAILING_DEMO_DIR);
			Constants.restartSchemas();			
		}
	}	
	
	/**
	 * Sets the inferred mental state on the GUI
	 * @param inferred the inferred mental state
	 */
	public void setInferredState(String inferred) {
		inferField.setText(inferred);
	}

	/**
	 * @return the holdingButton
	 */
	public JButton getHoldingButton() {
		return holdingButton;
	}

	/**
	 * @return the pryingButton
	 */
	public JButton getPryingButton() {
		return pryingButton;
	}

	/**
	 * @return the nailingButton
	 */
	public JButton getNailingButton() {
		return nailingButton;
	}
}
