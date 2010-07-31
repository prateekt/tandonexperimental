package ui.gui;

import javax.swing.*;
import java.awt.*;

/**
 * Represent different frame windows. All GUI frame windows extend this class.
 * This includes RealTimeView, ControllerView, etc.
 * @author Prateek Tandon
 *
 */
public class View extends JFrame {
		
	/**
	 * A listing of screens on this window.
	 */
	protected Screen[] screens;
		
	/**
	 * The view
	 * @param arm The arm model
	 * @param nail The nail model
	 */
	public View() {
	}
	
	/**
	 * Computes layout of window.
	 */
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
}
