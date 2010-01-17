package src.gui;

import javax.swing.*;
import java.awt.*;

import src.env_model.*;

public class View extends JFrame {
	
	//arm and nail
	protected HammeringArm arm;
	protected Nail nail;
	protected Screen[] screens;
		
	public View(HammeringArm arm, Nail nail) {
		this.arm = arm;
		this.nail = nail;
	}
	
	public void doLayout2() {
		getContentPane().setBackground(Color.WHITE);
		setLayout(null);
		int x=0, y=0, horScreensLaid = 0;
		for(Screen screen : screens) {
			getContentPane().add(screen);
			screen.setBounds(x,y,300,300);
			horScreensLaid++;
			x+=300;
			
			if(horScreensLaid==3) {
				horScreensLaid=0;
				x=0;
				y+=300;
			}			
		}
		
		int length=0;
		if(y >= 300) {
			length = 920;
		}
		else {
			length = horScreensLaid*300 + 20;
		}
		
		//boundary case
		if(y==0) {
			y=300;
		}
		
		setSize(length,y+50);
		setVisible(true);
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
