package src.gui;

import java.awt.Graphics;
import java.awt.*;
import javax.swing.JPanel;

import src.model.HammeringArm;
import src.model.Nail;

public class VisualizationScreen extends JPanel {
		
	private Nail nail;
	private HammeringArm arm;
		
	public VisualizationScreen(Nail nail, HammeringArm arm) {
		this.nail = nail;
		this.arm = arm;
		setBackground(Color.white);
		setLayout(null);
    	setSize(300,300);
	}
	
	public void paint(Graphics g) {
		nail.draw(g);
		arm.draw(g);
	}
}
