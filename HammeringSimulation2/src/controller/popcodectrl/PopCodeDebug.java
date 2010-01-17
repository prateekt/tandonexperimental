package src.controller.popcodectrl;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import src.gui.*;
import java.awt.*;
import javax.swing.*;

public class PopCodeDebug extends JPanel {
	/**
	 * The graph image to draw
	 */
	private Image myGraph = null;
	
	/**
	 * The parent frame of this panel.
	 */
	private Component owner;

	private XYSeries preferredValsSeries;
	
	/**
	 * The master series
	 */
	private XYSeriesCollection allSeries;
	
	/**
	 * The title for the plot
	 */
	private String title; 
	
	public PopCodeDebug(Component owner) {
    	preferredValsSeries = new XYSeries("Preferred Vals");
		allSeries = new XYSeriesCollection(preferredValsSeries);
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
        	("PopCode",  // Title
			"Neuron",           // X-Axis label
		     "Preferred Vals",           // Y-Axis label
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
    
   public void addPreferredValsSeriesPoint(double t, double val)  {
	   preferredValsSeries.add(t, val);
	   repaint();
   }
}