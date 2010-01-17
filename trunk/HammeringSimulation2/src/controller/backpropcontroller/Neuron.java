package src.controller.backpropcontroller;
import java.util.*;

public class Neuron {
	
	private int numInputs;
	private List<Double> vecWeight;
	
	public Neuron(int numInputs) {
		this.numInputs = numInputs;
		vecWeight = new ArrayList<Double>();
		
		//set the weights to initial random value
		//-1 < w < 1
		for(int x=0; x < numInputs+1; x++) {
			
			//generate sign
			double rand = Math.random();
			double sign=1;
			if(rand > 0.5) {
				sign=-1;
			}
			
			vecWeight.add(sign*Math.random());
		}
	}

	public int getNumInputs() {
		return numInputs;
	}

	public void setNumInputs(int numInputs) {
		this.numInputs = numInputs;
	}

	public List<Double> getVecWeight() {
		return vecWeight;
	}

	public void setVecWeight(List<Double> vecWreight) {
		this.vecWeight = vecWreight;
	}
}
