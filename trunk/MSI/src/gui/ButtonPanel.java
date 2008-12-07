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
	private JButton holdingButton;
	private JButton pryingButton;
	private JButton nailingButton;
	private GUI owner;
	
	public ButtonPanel(GUI owner) {
		this.owner = owner;
		setLayout(new FlowLayout());
		holdingButton = new JButton("Holding Demo");
		pryingButton = new  JButton("Prying Demo");
		nailingButton = new JButton("Nailing Demo");
		add(nailingButton);
		add(pryingButton);
		add(holdingButton);
		holdingButton.addActionListener(this);
		pryingButton.addActionListener(this);
		nailingButton.addActionListener(this);
	}
	
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
}
