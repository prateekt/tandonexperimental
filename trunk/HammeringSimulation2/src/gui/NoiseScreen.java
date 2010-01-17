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

public class NoiseScreen extends GraphScreen {
	
	/**
	 * The instantaneous plot series for noise
	 */
	private XYSeriesCache instNoiseSeries;

	/**
	 * The plot series for cumulative noise
	 */
	private XYSeriesCache cumNoiseSeries;
	
	/**
	 * @param owner
	 * @param axis
	 */
	public NoiseScreen(Component owner, String axis) {
		super(owner, "Hit Noise Profile", "Time", "Noise");
    	instNoiseSeries = new XYSeriesCache("Instantaneous Noise: " + axis + " Axis");
    	cumNoiseSeries = new XYSeriesCache("Cumulative Noise: " + axis + " Axis");
    	addSeries(instNoiseSeries);
    	addSeries(cumNoiseSeries);
	}

	public void addInstNoiseSeriesPoint(double t, double val)  {	
		//cyclic property
		if(instNoiseSeries.contains(t)) {
			resetInstNoiseSeries();
		}
		instNoiseSeries.add(t, val);
		repaint();
	}
	
	public void resetInstNoiseSeries() {
		instNoiseSeries.clear();
		repaint();
	}
   
	public void addCumNoiseSeriesPoint(double t, double val) {
		//cyclic property
		if(cumNoiseSeries.contains(t)) {
			resetCumNoiseSeries();
		}
		cumNoiseSeries.add(t,val);
		repaint();
	}
	
	public void resetCumNoiseSeries() {
		cumNoiseSeries.clear();
		repaint();
	}
}