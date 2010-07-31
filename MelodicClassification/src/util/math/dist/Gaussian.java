package util.math.dist;
import java.util.*;

public class Gaussian {
	
	public List<Double> dataSet;
	private double mean;
	private double stddev;
	
	public Gaussian(double mean, double stddev) {
		this.mean = mean;
		this.stddev = stddev;
		this.dataSet = new ArrayList<Double>();
	}
	
	public Gaussian(List<Double> dataSet) {
		this.dataSet = dataSet;
		mean = computeMean();
		stddev = computeStandardDeviation();
	}
	
	public List<Integer> applyStdDevFilter(double numStdDev) {
		List<Integer> rtn = new ArrayList<Integer>();
		for(int x=0; x < dataSet.size(); x++) {
			double d= dataSet.get(x);
			if(d > mean + numStdDev*stddev)
				continue;
			if(d < mean - numStdDev*stddev)
				continue;
			
			//kill
			rtn.add(x);
		}
		return rtn;
	}
	
	public double computeMean() {
		if(dataSet.size()==0)
			return 0.0;
		
		//compute mean
		double sum=0.0;
		for(Double d : dataSet) {
			sum+= d;
		}
		return sum/dataSet.size();
	}
	
    public double computeStandardDeviation() {  
    	if(dataSet.size()==0)
    		return 0.0;
    	
    	double mean = computeMean();    	
    	double squareSum=0.0;
    	for(int x=0; x < dataSet.size(); x++) {
    		squareSum+= Math.pow(dataSet.get(x),2);
    	}
    	
    	return Math.sqrt(squareSum/dataSet.size() - mean*mean );
   }
    
	/**
	 * Computes a gaussian.
	 * @param x The x parameter
	 * @param mean The mean parameter
	 * @param stddev The stddev parameter
	 * @return The gaussian value
	 */
	public double computeGaussian(double x) {
		double expTerm = -1*Math.pow(x-mean, 2) / (2*stddev*stddev);
		return 1/(stddev*Math.sqrt(2*Math.PI)) * Math.exp(expTerm);
	}

	/**
	 * @return the dataSet
	 */
	public List<Double> getDataSet() {
		return dataSet;
	}

	/**
	 * @param dataSet the dataSet to set
	 */
	public void setDataSet(List<Double> dataSet) {
		this.dataSet = dataSet;
	}

	/**
	 * @return the mean
	 */
	public double getMean() {
		return mean;
	}

	/**
	 * @param mean the mean to set
	 */
	public void setMean(double mean) {
		this.mean = mean;
	}

	/**
	 * @return the stddev
	 */
	public double getStddev() {
		return stddev;
	}

	/**
	 * @param stddev the stddev to set
	 */
	public void setStddev(double stddev) {
		this.stddev = stddev;
	}		
}
