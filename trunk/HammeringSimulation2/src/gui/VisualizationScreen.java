package src.gui;

import java.awt.*;
import javax.swing.JPanel;

import src.env_model.HammeringArm;
import src.env_model.Nail;



public class VisualizationScreen extends Screen {
				
	public VisualizationScreen(HammeringArm arm, Nail nail) {
		super(arm, nail);
	}
	
	public void paint(Graphics g) {
		nail.draw(g);
		arm.draw(g);
	}
}
