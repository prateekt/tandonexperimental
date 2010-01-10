package src.main;

import src.gui.View;
import src.model.HammeringArm;
import src.model.Nail;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Nail n = new Nail(20,20,40,5);
		HammeringArm h = new HammeringArm(100,100,20,20);
		View v = new View(n,h);
		h.setView(v);
		h.setNail(n);
		h.executeSwing(80, 1);
		
/*		while(true) {
			try {
				Thread.sleep(1000);
				//n.hit(20, 1);
				v.repaint();
			}
			catch(Exception e) {
				e.printStackTrace();
			
			}
		}*/
	}
}
