package util.math.dist;

import java.util.*;

import util.sys.SysUtil;

public class ExtremaMath {
	
	public static void main(String[] args) {
		List<Double> list = SysUtil.getArray("tl--1_time_domain.txt");
		List<Extrema> localE = getLocalExtrema(list);		
	}
		
	public static double[] findClosestSampleSize_value_stddev(List<Extrema> list, int target ,int maxIter) {

		//filter
		double scale =1;
		double start =0;
		double[] rtn=null;
		for(int x=1; x<maxIter; x++) {
			double[] point = findTargetSizeHelper(list, start, scale,target);
			if(point[1] < target){}
			else {
				start = point[0];
			}
			scale = 1/(Math.pow(10, x));
			rtn = point;
		}		
		
		return rtn;
	}
	
	private static double[] findTargetSizeHelper(List<Extrema> list, double start, double scale, int target) {
		double x=start;
		int size=list.size();
		int trail_size=-1;
		while(size>target) {
			List<Extrema> r = applyStdDevFilter(list, x);
			trail_size = size;
			size = r.size();
			x=x+scale;
		}
		
		double[] rtn = new double[2];
		rtn[0] = x-2*scale;
		rtn[1] = trail_size;
		return rtn;
	}
		
	public static List<Extrema> applyStdDevFilter(List<Extrema> extrema, double numStdDev) {
		List<Extrema> rtn = new ArrayList<Extrema>();

		//create Gaussian
		List<Double> dList = new ArrayList<Double>();
		for(Extrema e : extrema) {
			dList.add(e.getValue());
		}
		Gaussian g = new Gaussian(dList);
		double mean = g.getMean();
		double stddev = g.getStddev();
		
		//use gaussian to filter
		List<Integer> toRemoveIndices = g.applyStdDevFilter(numStdDev);
		for(int x=0; x < extrema.size(); x++) {
			if(!toRemoveIndices.contains(x))
				rtn.add(extrema.get(x));
		}
		
		return rtn;
	}
		
	public static List<Extrema> arrToList(Extrema[] arr) {
		List<Extrema> rtn = new ArrayList<Extrema>();
		for(Extrema e : arr) {
			rtn.add(e);
		}
		return rtn;
	}
	
	public static List<Extrema> getLocalExtrema(List<Double> values) {
		List<Extrema> rtn = new ArrayList<Extrema>();
		
		double trail_1=Double.NaN;
		double trail_2=Double.NaN;
		for(int x=0; x < values.size(); x++) {
			double v = values.get(x);
			
			if(trail_1!=Double.NaN && trail_2!=Double.NaN) {
				
				double lastDiff = trail_1 - trail_2;
				double currentDiff = v - trail_1;
				
				if(lastDiff > 0 && currentDiff < 0) {
					Extrema newE = new Extrema(true, trail_1, x-1);
					rtn.add(newE);
				}
				if(lastDiff < 0 && currentDiff > 0) {
					Extrema newE = new Extrema(false,trail_1,x-1);
					rtn.add(newE);
				}				
			}
			
			//update
			trail_2 = trail_1;
			trail_1 = v;
		}
		return rtn;		
	}
	
	public static Extrema[] computeNMaxima(List<Double> values, int n) {
		
		//they got nothin
		if(values==null || values.size()==0 || n < 0 || n > values.size()) {
			return null;
		}
		
		//init extrema
		Extrema[] rtn = new Extrema[n];
		for(int x=0; x < rtn.length; x++) {
			rtn[x] = new Extrema(true);
		}
		
		//iterate
		for(int x=0; x < values.size(); x++) {
			double currentVal = values.get(x);
			
			spotfinder:for(int y=0; y < rtn.length; y++) {
				
				//found spot
				if(currentVal > rtn[y].getValue()) {
					
					//propogate down
					for(int z=rtn.length-1; z > y; z--) {
						rtn[z] = rtn[z-1];
					}
					
					//new
					rtn[y] = new Extrema(true);
					rtn[y].setIndex(x);
					rtn[y].setValue(currentVal);
					
					//break
					break spotfinder;
				}
			}
		}
		return rtn;		
	}
	
	public static Extrema[] computeNMinima(List<Double> values, int n) {
		
		//they got nothin
		if(values==null || values.size()==0 || n < 0 || n > values.size()) {
			return null;
		}
		
		//init extrema
		Extrema[] rtn = new Extrema[n];
		for(int x=0; x < rtn.length; x++) {
			rtn[x] = new Extrema(false);
		}
		
		//iterate
		for(int x=0; x < values.size(); x++) {
			double currentVal = values.get(x);
			
			spotfinder:for(int y=0; y < rtn.length; y++) {
				
				//found spot
				if(currentVal < rtn[y].getValue()) {
					
					//propogate down
					for(int z=rtn.length-1; z > y; z--) {
						rtn[z] = rtn[z-1];
					}
					
					//new
					rtn[y] = new Extrema(true);
					rtn[y].setIndex(x);
					rtn[y].setValue(currentVal);
					
					//break
					break spotfinder;
				}
			}
		}
		return rtn;		
	}
	
	public static Extrema[] computeThreeMaxima(List<Double> values) {
		Extrema max = new Extrema(true);
		Extrema secondMax = new Extrema(true);
		Extrema thirdMax = new Extrema(true);
		
		for(int x=0; x < values.size(); x++) {
			double currentVal = values.get(x);
			
			//easy exit
			if(currentVal < thirdMax.getValue()) {
				continue;
			}
			
			//tough sell
			if(currentVal > max.getValue()) {

				//propagate down
				thirdMax = secondMax;
				secondMax = max;
				
				//set new max
				max = new Extrema(true);
				max.setIndex(x);
				max.setValue(currentVal);				
			}
			else if(currentVal > secondMax.getValue()) {
				
				//propagate down
				thirdMax = secondMax;
				
				//set new second max
				secondMax = new Extrema(true);
				secondMax.setIndex(x);
				secondMax.setValue(currentVal);
				
			}
			else if(currentVal > thirdMax.getValue()){
				
				//set new third max
				thirdMax = new Extrema(true);
				thirdMax.setIndex(x);
				thirdMax.setValue(currentVal);
			}
		}
		
		Extrema[] rtn = new Extrema[3];
		rtn[0] = max;
		rtn[1] = secondMax;
		rtn[2] = thirdMax;
		
		return rtn;
	}		
}
