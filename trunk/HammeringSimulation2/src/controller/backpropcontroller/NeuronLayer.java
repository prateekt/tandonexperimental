package src.controller.backpropcontroller;
import java.util.*;

public class NeuronLayer {
	
	private int numNeurons;
	private List<Neuron> vecNeurons;
	
	public NeuronLayer(int numNeurons, int numInputsPerNeuron) {
		this.numNeurons = numNeurons;
		vecNeurons = new ArrayList<Neuron>();
		for(int x=0; x < numNeurons; x++) {
			vecNeurons.add(new Neuron(numInputsPerNeuron));
		}
	}

	public int getNumNeurons() {
		return numNeurons;
	}

	public void setNumNeurons(int numNeurons) {
		this.numNeurons = numNeurons;
	}

	public List<Neuron> getVecNeurons() {
		return vecNeurons;
	}

	public void setVecNeurons(List<Neuron> vecNeurons) {
		this.vecNeurons = vecNeurons;
	}
}
