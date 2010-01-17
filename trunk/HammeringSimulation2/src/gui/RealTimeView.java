package src.gui;

import javax.swing.*;


import java.awt.*;
import javax.swing.JPanel;
import org.jfree.chart.ChartUtilities;
import java.io.*;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import src.env_model.HammeringArm;
import src.env_model.Nail;


import java.io.*;
import java.awt.Image;
import java.awt.image.BufferedImage;

/**
 * @author Prateek Tandon
 */
public class RealTimeView extends View {
		
	public RealTimeView(HammeringArm arm,Nail nail) {
		super(arm, nail);
		screens = new Screen[3];
		screens[0] = new VisualizationScreen(arm,nail);
		screens[1] = new NoiseScreen(this, "Z");
		screens[2] = new BendScreen(arm,nail);
		doLayout2();
	}


	public VisualizationScreen getVisScreen() {
		return (VisualizationScreen) screens[0];
	}

	public NoiseScreen getNoiseScreen() {
		return (NoiseScreen) screens[1];
	}

	public BendScreen getBendScreen() {
		return (BendScreen) screens[2];
	}
}