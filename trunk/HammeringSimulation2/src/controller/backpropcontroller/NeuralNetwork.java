package src.controller.backpropcontroller;
import java.util.*;
public class NeuralNetwork {
	
	private int numInputs;
	private int numOutputs;
	private int numHiddenLayers;
	private int neuronsPerHiddenLayer;
	private double bias;
	private double activationResponse;
	private List<NeuronLayer> vecLayers;
	
	public NeuralNetwork(int numInputs, int numOutputs, int numHiddenLayers, int neuronsPerHiddenLayer, double bias, double activationResponse) {
		this.numInputs = numInputs;
		this.numOutputs = numOutputs;
		this.numHiddenLayers = numHiddenLayers;
		this.neuronsPerHiddenLayer = neuronsPerHiddenLayer;
		this.bias = bias;
		this.activationResponse = activationResponse;
		
		//create net
		createNet();
	}
	
	public void createNet() {
		vecLayers = new ArrayList<NeuronLayer>();
		
		if(numHiddenLayers > 0 ) {
			//first hidden layer
			vecLayers.add(new NeuronLayer(neuronsPerHiddenLayer, numInputs));
		
			//all others
			for(int x=0; x < numHiddenLayers-1; x++) {
				vecLayers.add(new NeuronLayer(neuronsPerHiddenLayer, neuronsPerHiddenLayer));
			}
		
			//output layer
			vecLayers.add(new NeuronLayer(numOutputs, neuronsPerHiddenLayer));
		}
		else {
			//create output layer
			vecLayers.add(new NeuronLayer(numOutputs, numInputs));
		}
	}	
	
	public List<Double> getWeights() {
		List<Double> weights = new ArrayList<Double>();
		
		for(int i=0; i < numHiddenLayers + 1; i++) {
			for(int j=0; j < vecLayers.get(i).getNumNeurons(); j++) {
				for(int k=0; k < vecLayers.get(i).getVecNeurons().get(j).getNumInputs(); k++) {
					weights.add(vecLayers.get(i).getVecNeurons().get(j).getVecWeight().get(k));
				}
			}
		}
		
		return weights;
	}
	
	public double sigmoid(double netinput, double response) {
		return (1 / (1 + Math.exp(-netinput / response)));
	}
	
	public List<Double> update(List<Double> inputs)	{
		
		//stores the resultant outputs from each layer
		List<Double> outputs = new ArrayList<Double>();
		int cWeight = 0;
		
		//first check that we have the correct amount of inputs
		if (inputs.size() != numInputs) {

			//just return an empty vector if incorrect.
			return outputs;
		}
		
		//For each layer....
		for (int i=0; i< numHiddenLayers + 1; i++) {		
			if ( i > 0 ) {
				inputs = outputs;
			}

			outputs.clear();
			
			cWeight = 0;

			//for each neuron sum the (inputs * corresponding weights).Throw 
			//the total at our sigmoid function to get the output.
			for (int j=0; j< vecLayers.get(i).getNumNeurons(); j++) {
				double netinput = 0;

				int	NumInputs = vecLayers.get(i).getVecNeurons().get(j).getNumInputs();
				
				//for each weight
				for (int k=0; k< NumInputs - 1; ++k)
				{
					//sum the weights x inputs
					netinput += vecLayers.get(i).getVecNeurons().get(j).getVecWeight().get(k) * inputs.get(cWeight++);
				}

				//add in the bias
				netinput += vecLayers.get(i).getVecNeurons().get(j).getVecWeight().get(NumInputs-1) * bias;

				//we can store the outputs from each layer as we generate them. 
				//The combined activation is first filtered through the sigmoid 
				//function
				outputs.add(sigmoid(netinput, activationResponse));
				cWeight = 0;
			}
		}

		return outputs;
	}

	public static void main(String[] args) {
	//	NeuralNetwork n = new NeuralNetwork()
	}
}
