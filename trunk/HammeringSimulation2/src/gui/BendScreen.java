package src.gui;

import java.awt.Graphics;
import src.env_model.HammeringArm;
import src.env_model.Nail;

public class BendScreen extends Screen {
	
	public BendScreen(HammeringArm arm, Nail nail) {
		super(arm, nail);
		this.nail = nail;
		this.arm = arm;
	}
	
	public void paint(Graphics g) {
		nail.draw_bend(g);
	}
}
