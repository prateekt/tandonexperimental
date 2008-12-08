package gui;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;
import org.jfree.chart.ChartUtilities;
import java.io.*;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

/**
 * Panel to draw forward model summaries in.
 * @author Prateek Tandon
 */
public class TriGraphPanel extends JPanel {
	
	/**
	 * The graph image to draw
	 */
	private Image myGraph = null;
	
	/**
	 * The parent frame of this panel.
	 */
	private Component owner;

	/**
	 * The plot series for the holding forward model.
	 */
	private XYSeries holdingSeries;
	
	/**
	 * The plot series for the nailing forward model.
	 */
	private XYSeries nailingSeries;

	/**
	 * The plot series for the prying forward model.
	 */
	private XYSeries pryingSeries;
	
	/**
	 * The master plot for the series
	 */
	protected XYSeriesCollection allSeries;

	/**
	 * The title of the graph.
	 */
	protected String series;
	
	/**
	 * Constructor
	 * @param owner The parent frame
	 * @param series The title of the graph
	 */
    public TriGraphPanel(Component owner, String series) {
    	this.series = series;
    	holdingSeries = new XYSeries("Holding Prediction: " + series);
    	nailingSeries = new XYSeries("Nailing Prediction: " + series);
    	pryingSeries = new XYSeries("Prying Prediction: " + series);
		allSeries = new XYSeriesCollection(holdingSeries);
		allSeries.addSeries(nailingSeries);
		allSeries.addSeries(pryingSeries);
    	setLayout(null);
    	setSize(300,300);
    	this.owner = owner;
    }
    
    /**
     * The method to draw the graph.
     */
    public void paint(Graphics g) {
    	myGraph = generateCurrentChart();
    	if (myGraph != null) {
			g.drawImage(myGraph, 0, 0, 300,300,owner);
      	}
    }
    
    /**
     * Generates the current chart based on the 
     * series data. Returns the chart as an image that
     * can be drawn in this panel.
     * @return The current chart
     */
    public BufferedImage generateCurrentChart() {
		JFreeChart chart = ChartFactory.createXYLineChart
        	(series,  // Title
			"Time",           // X-Axis label
		     series,           // Y-Axis label
		     allSeries,          // Dataset
		     PlotOrientation.VERTICAL,
		     true,
		     true,
		     true 
        );

        try {
            ChartUtilities.saveChartAsJPEG(new File(series+".jpg"), chart, 300,
                300);
        } catch (Exception e) {
            System.out.println("Problem occurred creating chart.");
        }
		return chart.createBufferedImage(300, 300);
    }
    
    /**
     * Add a point to the holding forward model series.
     * @param t The time of new point
     * @param val The dependent variable
     */
    public void addHoldingSeriesPoint(double t, double val) {
    	holdingSeries.add(t, val);
    	repaint();
    }
    
    /**
     * Add a  point to the nailing forward model series.
     * @param t The time of new point
     * @param val The dependent variable
     */
    public void addNailingSeriesPoint(double t, double val)  {
    	nailingSeries.add(t, val);
    	repaint();
    }
    	
    /**
     * Add a point to the prying forward model series.
     * @param t The time of new point
     * @param val The dependent variable
     */
    public void addPryingSeriesPoint(double t, double val)  {
    	pryingSeries.add(t, val);
    	repaint();
    }
    
    public void reset() {
    	holdingSeries = new XYSeries("Holding Prediction: " + series);
    	nailingSeries = new XYSeries("Nailing Prediction: " + series);
    	pryingSeries = new XYSeries("Prying Prediction: " + series);
		allSeries = new XYSeriesCollection(holdingSeries);
		allSeries.addSeries(nailingSeries);
		allSeries.addSeries(pryingSeries);
    }
}
