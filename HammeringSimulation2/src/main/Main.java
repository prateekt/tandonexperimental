package src.main;

import src.gui.View;
import src.model.HammeringArm;
import src.model.Nail;
import src.rlcontroller.*;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Nail n = new Nail(20,20,40,1);
		HammeringArm h = new HammeringArm(100,100,20,2);
		View v = new View(n,h);
		h.setView(v);
		h.setNail(n);
		n.setArm(h);
		
		for(int x=0; x < 500; x++) {
			RLController2 rlctrl = new RLController2(n,h);
			rlctrl.qlearning(50);
			n.reset();
			h.reset();
		}
		
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
