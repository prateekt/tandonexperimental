package ui.gui;

import java.awt.Color;
import javax.swing.*;

/**
 * Representation of a data or drawing window. All subwindows elements
 * extend this. The hierarchy is GraphScreen and NoiseScreen extend Screen.
 * Specific graphs extend GraphScreen. 
 * @author Prateek Tandon
 *
 */
public class Screen extends JPanel {
		
	/**
	 * The screen constructor
	 * @param arm The arm model
	 * @param nail The nail model
	 */
	public Screen() {
		setBackground(Color.white);
		setLayout(null);
		setSize(300,300);
	}

}
