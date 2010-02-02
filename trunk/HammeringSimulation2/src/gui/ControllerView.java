package src.gui;

import java.awt.Component;

import src.env_model.*;
import org.jfree.data.xy.XYSeries;
import src.gui.*;

public class ControllerView extends View {
	
	private XYSeriesCache rl, pop;
	
	public ControllerView(HammeringArm arm, Nail nail) {
		super(arm, nail);
		screens = new Screen[1];
		screens[0] = new GraphScreen(this, "Nails hammered by Hits for Nail", "Number of Nails", "Hits for Nail");
		
		//save params
		((GraphScreen)screens[0]).setSave(true);
		((GraphScreen)screens[0]).setSaveFileName("Controller.jpg");
		
//		rl = new XYSeriesCache("RL Controller");
		pop = new XYSeriesCache("Pop Code Controller");
//    	((GraphScreen)screens[0]).addSeries(rl);
    	((GraphScreen)screens[0]).addSeries(pop);
    	doLayout2();
	}
	
	public void addRLPoint(int numNails, int hits) {
		rl.add(numNails, hits);
		repaint();
	}
	
	public void addPopPoint(int numNails, int hits) {
		pop.add(numNails, hits);
		repaint();
	}
}
