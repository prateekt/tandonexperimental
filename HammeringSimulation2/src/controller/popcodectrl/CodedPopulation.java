package src.controller.popcodectrl;

import java.util.*;
import javax.swing.*;
import java.awt.*;

public class CodedPopulation {

	private double[] preferredVals;
	private double[] firingRate;
	private double maxValueForPopulation;
	private int numNeurons;
	private CodedPopulationView view;
	
	public CodedPopulation(String populationTitle, int numNeurons, double maxValueForPopulation) {
		this.numNeurons = numNeurons;
		preferredVals = new double[numNeurons];
		firingRate = new double[numNeurons];
		view = new CodedPopulationView(populationTitle);
		
		for(int x=0; x < numNeurons; x++) {
			double val =  maxValueForPopulation/numNeurons*x;
			preferredVals[x] = val;
			if(view!=null)
				view.addPreferredVal(x, preferredVals[x]);
		}		
	}
	
	public double getStdDev(double[] vars) {
		
		//get mean of numbers
		double sum=0.0;
		for(double var : vars) {
			sum+=var;
		}
		double mean = sum / vars.length;
		
		//mean of squares
		sum=0.0;
		for(double var : vars) {
			sum+= Math.pow(var-mean, 2);
		}
		return Math.sqrt(sum/(vars.length));
	}
	
	public void encodeValue(double value) {
		double sigma = getStdDev(preferredVals);
//		System.out.println("SIGMA: " + sigma);
//		double sigma = preferredVals.length;
		for(int z=0; z < numNeurons; z++) {
			double p = preferredVals[z];
			double x = value;
			double coeff = 1 / Math.sqrt(2*Math.PI*Math.pow(sigma, 2));
			firingRate[z] = coeff*Math.exp((-1*Math.pow(p-x,2.0)) / (2*Math.pow(sigma,2.0)));

			if(view!=null) {
				view.addFiringRateVal(z, firingRate[z]);
			}
	//		debugScreen.addPreferredValsSeriesPoint(z, firingRate[z]);
			//			System.out.println("FIRING RATE: " + firingRate[z]);
		}
	}
		
	public double decodeValue_CenterOfMass() {
		
		//compute s (sum of firing rates)
		double S = 0.0;
		for(int x=0; x < numNeurons; x++) {
			S+= firingRate[x];			
		}
//		System.out.println("S: " + S);
		double rtn=0.0;
		for(int z=0; z < numNeurons; z++) {
			rtn+=firingRate[z]/S*preferredVals[z]; 
		}
		
		return rtn;
	}
	
	public double decodeValue_max() {
		double max = -1;
		for(int x=0; x < firingRate.length; x++) {
			if(firingRate[x] > max) {
				max = firingRate[x];
			}
		}
		
		return max;
	}
	
	public double[] getPreferredVals() {
		return preferredVals;
	}

	public void setPreferredVals(double[] preferredVals) {
		this.preferredVals = preferredVals;
	}

	public double[] getFiringRate() {
		return firingRate;
	}

	public void setFiringRate(double[] firingRate) {
		this.firingRate = firingRate;
		if(view!=null) {
			for(int z=0; z < firingRate.length; z++) {
				view.addFiringRateVal(z, firingRate[z]);
			}
			view.repaint();
		}
	}

	public int getNumNeurons() {
		return numNeurons;
	}

	public void setNumNeurons(int numNeurons) {
		this.numNeurons = numNeurons;
	}

	public static void main(String[] args) {
		CodedPopulation c = new CodedPopulation("Test Population", 1000, 180);
		c.encodeValue(160);
		System.out.println("MAX: " + c.decodeValue_max());
		System.out.println("COM: " + c.decodeValue_CenterOfMass());
	}
}
