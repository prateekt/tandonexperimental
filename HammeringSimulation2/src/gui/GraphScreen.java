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
import java.util.*;
import java.awt.*;

public class GraphScreen extends Screen {
	
	/**
	 * The graph image to draw
	 */
	protected Image myGraph = null;
	
	/**
	 * The parent frame of this panel.
	 */
	protected Component owner;
	
	/**
	 * The master series
	 */
	protected XYSeriesCollection allSeries;
	
	/**
	 * The title for the plot
	 */
	protected String title; 
	
	/**
	 * X Axis Label
	 */
	protected String xAxisLabel;
	
	/**
	 * Y Axis Label
	 */
	protected String yAxisLabel;
		
	/**
	 * Whether to save the graph or not to a file upon redraw.
	 */
	protected boolean save;
	
	/**
	 * The file name to save to.
	 */
	protected String saveFileName;
	
	/**
	 * CTR
	 * @param owner
	 * @param title
	 * @param xAxisLabel
	 * @param yAxisLabel
	 */
	public GraphScreen(Component owner, String title, String xAxisLabel, String yAxisLabel) {
		super(null,null);
		allSeries = new XYSeriesCollection();
		this.owner = owner;
		this.title = title;
		this.xAxisLabel = xAxisLabel;
		this.yAxisLabel = yAxisLabel;
		save = false;
		saveFileName = "";
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
        	(title,  // Title
			 xAxisLabel,           // X-Axis label
		     yAxisLabel,           // Y-Axis label
		     allSeries,          // Dataset
		     PlotOrientation.VERTICAL,
		     true,
		     true,
		     true 
        );
		
		if(save) {
	        try {
	            ChartUtilities.saveChartAsJPEG(new File(saveFileName), chart, 300, 300);
	        } catch (Exception e) {
	            System.out.println("Problem occurred creating chart.");
	        }
		}
		
		return chart.createBufferedImage(300, 300);
   }

	public Image getMyGraph() {
		return myGraph;
	}

	public void setMyGraph(Image myGraph) {
		this.myGraph = myGraph;
	}

	public Component getOwner() {
		return owner;
	}

	public void setOwner(Component owner) {
		this.owner = owner;
	}

	public XYSeriesCollection getAllSeries() {
		return allSeries;
	}

	public void setAllSeries(XYSeriesCollection allSeries) {
		this.allSeries = allSeries;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getXAxisLabel() {
		return xAxisLabel;
	}

	public void setXAxisLabel(String axisLabel) {
		xAxisLabel = axisLabel;
	}

	public String getYAxisLabel() {
		return yAxisLabel;
	}

	public void setYAxisLabel(String axisLabel) {
		yAxisLabel = axisLabel;
	}

	public boolean isSave() {
		return save;
	}

	public void setSave(boolean save) {
		this.save = save;
	}

	public String getSaveFileName() {
		return saveFileName;
	}

	public void setSaveFileName(String saveFileName) {
		this.saveFileName = saveFileName;
	} 
	
	public void addSeries(XYSeries series) {
		allSeries.addSeries(series);
		repaint();
	}
	
	public void removeSeries(int seriesIndex) {
		allSeries.removeSeries(seriesIndex);
		repaint();
	}
}