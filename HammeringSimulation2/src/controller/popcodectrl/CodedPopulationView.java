package src.controller.popcodectrl;
import java.awt.Component;

import src.gui.*;


public class CodedPopulationView extends View {
	
	/**
	 * Preferred Value Series
	 */
	private XYSeriesCache preferredValsSeries;

	/**
	 * Firing Rate series
	 */
	private XYSeriesCache firingRateSeries;

	public CodedPopulationView(String populationTitle) {
		super(null,null);
		setTitle(populationTitle);
		screens = new Screen[2];
		screens[0] = new GraphScreen(this, "Preferred Values", "Neuron Number", "Preferred Value");
		screens[1] = new GraphScreen(this, "Firing Rate", "Neuron Number", "Firing Rate");
		preferredValsSeries = new XYSeriesCache("Preferred Vals");
		firingRateSeries = new XYSeriesCache("Firing Rate");
		((GraphScreen)screens[0]).addSeries(preferredValsSeries);
		((GraphScreen)screens[1]).addSeries(firingRateSeries);
		doLayout2();
	}
	
	public void addPreferredVal(double neuronNum, double pref) {
		if(neuronNum==0) {
			preferredValsSeries.clear();
		}
		preferredValsSeries.add(neuronNum, pref);
		repaint();
	}
	
	public void addFiringRateVal(double neuronNum, double rate) {
		if(neuronNum==0) {
			firingRateSeries.clear();
		}
		firingRateSeries.add(neuronNum, rate);
		repaint();
	}
}
