package src.gui;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.chart.util.*;

public class NoiseScreen extends JPanel {
	
	/**
	 * The graph image to draw
	 */
	private Image myGraph = null;
	
	/**
	 * The parent frame of this panel.
	 */
	private Component owner;

	/**
	 * The instantaneous plot series for noise
	 */
	private XYSeries instNoiseSeries;

	/**
	 * The plot series for cumulative noise
	 */
	private XYSeries cumNoiseSeries;
	
	/**
	 * The master series
	 */
	private XYSeriesCollection allSeries;
	
	/**
	 * The title for the plot
	 */
	private String title; 
	
	public NoiseScreen(Component owner, String axis) {
    	instNoiseSeries = new XYSeries("Instantaneous Noise: " + axis + " Axis");
    	cumNoiseSeries = new XYSeries("Cumulative Noise: " + axis + " Axis");
		allSeries = new XYSeriesCollection(instNoiseSeries);
		allSeries.addSeries(cumNoiseSeries);
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
        	("Noise Profile",  // Title
			"Time",           // X-Axis label
		     "Noise",           // Y-Axis label
		     allSeries,          // Dataset
		     PlotOrientation.VERTICAL,
		     true,
		     true,
		     true 
        );

/*        try {
            ChartUtilities.saveChartAsJPEG(new File("noise.jpg"), chart, 300,
                300);
        } catch (Exception e) {
            System.out.println("Problem occurred creating chart.");
        }*/
		return chart.createBufferedImage(300, 300);
   }
    
   public void addInstNoiseSeriesPoint(double t, double val)  {
	   instNoiseSeries.add(t, val);
	   repaint();
   }
   
   public void addCumNoiseSeriesPoint(double t, double val) {
	   cumNoiseSeries.add(t,val);
	   repaint();
   }
}