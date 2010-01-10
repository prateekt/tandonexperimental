package src.gui;

import javax.swing.*;

import src.model.HammeringArm;
import src.model.Nail;

import java.awt.*;
import javax.swing.JPanel;
import org.jfree.chart.ChartUtilities;
import java.io.*;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import java.io.*;
import java.awt.Image;
import java.awt.image.BufferedImage;

/**
 * @author Prateek Tandon
 */
public class View extends JFrame {

	private Nail nail;
	private HammeringArm arm;
	
	private VisualizationScreen visScreen;
	private GraphScreen graphScreen;
	
	public View(Nail nail, HammeringArm arm) {
		this.nail = nail;
		this.arm = arm;
		visScreen = new VisualizationScreen(nail, arm);
		graphScreen = new GraphScreen(this, "Z");
		getContentPane().setBackground(Color.WHITE);
		setLayout(null);
		getContentPane().add(visScreen);
		getContentPane().add(graphScreen);
		
		visScreen.setBounds(0,0,300,300);
		graphScreen.setBounds(300,0,300,300);
	
		setSize(610,350);
		setVisible(true);
	}

	public Nail getNail() {
		return nail;
	}

	public void setNail(Nail nail) {
		this.nail = nail;
	}

	public HammeringArm getArm() {
		return arm;
	}

	public void setArm(HammeringArm arm) {
		this.arm = arm;
	}

	public VisualizationScreen getVisScreen() {
		return visScreen;
	}

	public void setVisScreen(VisualizationScreen visScreen) {
		this.visScreen = visScreen;
	}

	public GraphScreen getGraphScreen() {
		return graphScreen;
	}

	public void setGraphScreen(GraphScreen graphScreen) {
		this.graphScreen = graphScreen;
	}
}