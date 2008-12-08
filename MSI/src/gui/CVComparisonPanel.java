package gui;

import java.awt.Component;

import org.jfree.data.xy.XYSeries;

/**
 * Extends TriGraphPanel to include the capability to
 * include a series for observed variables, as opposed to
 * just simulated forward model variables.
 * @author Prateek Tandon
 *
 */
public class CVComparisonPanel extends TriGraphPanel {
		
	/**
	 * Observed series
	 */
	private XYSeries obsSeries;
	
	/**
	 * The constructor
	 * @param owner The parent frame
	 * @param series The title for the graph
	 */
	public CVComparisonPanel(Component owner, String series) {
		super(owner, series);
    	obsSeries = new XYSeries("Observed " + series);
    	allSeries.addSeries(obsSeries);
	}
	
	/**
	 * Add new observed variable
	 * @param t The time of the update
	 * @param val The dependent variable
	 */
	public void addObservedSeriesPoint(double t, double val)  {
		obsSeries.add(t,val);
		repaint();
	}
	
	public void reset() {
		super.reset();
		obsSeries = new XYSeries("Observed " + series);
		allSeries.addSeries(obsSeries);
	}
}