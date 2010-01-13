package src.gui;

import java.awt.Color;
import java.awt.Graphics;

import src.model.HammeringArm;
import src.model.Nail;
import java.awt.*;
import javax.swing.*;

public class BendScreen extends JPanel {
	private Nail nail;
	private HammeringArm arm;
		
	public BendScreen(Nail nail, HammeringArm arm) {
		this.nail = nail;
		this.arm = arm;
		setBackground(Color.white);
		setLayout(null);
    	setSize(300,300);
	}
	
	public void paint(Graphics g) {
		nail.draw_bend(g);
	}
}
