package src.gui;

import java.awt.Color;
import javax.swing.*;
import src.env_model.*;

public class Screen extends JPanel {
	
	//Nail, arm
	protected HammeringArm arm;
	protected Nail nail;
	
	public Screen(HammeringArm arm, Nail nail) {
		this.arm = arm;
		this.nail = nail;
		setBackground(Color.white);
		setLayout(null);
		setSize(300,300);
	}

	public HammeringArm getArm() {
		return arm;
	}

	public void setArm(HammeringArm arm) {
		this.arm = arm;
	}

	public Nail getNail() {
		return nail;
	}

	public void setNail(Nail nail) {
		this.nail = nail;
	}
}
