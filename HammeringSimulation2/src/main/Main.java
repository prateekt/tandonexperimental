package src.main;

import src.controller.*;
import src.controller.popcodectrl.*;
import src.controller.rlcontroller.*;
import src.env_model.HammeringArm;
import src.env_model.Nail;
import src.gui.*;

public class Main {
	
	public static int NAIL_NUMBER=0;
	public static int TOTAL_NAILS=0;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Nail n = new Nail(20,20,40,1);
		HammeringArm h = new HammeringArm(100,100,20,2);
//		RealTimeView v = new RealTimeView(h,n);
		Controller rlctrl = new PopulationNetworkController(h,n,10);
		ControllerView v = new ControllerView(h,n);
		rlctrl.setControllerView(v);
		h.setNail(n);
		n.setArm(h);
		
		//500 nails
		TOTAL_NAILS=500;
		for(int x=0; x < 500; x++) {
			NAIL_NUMBER = x;
			rlctrl.setNailNumber(x);
			//50 time steps
			rlctrl.control(50);
			n.reset();
			h.reset();
		}
	}
}