package src.controller.popcodectrl;
import src.controller.*;
import src.env_model.*;

public class PopulationNetworkController extends Controller {

	//controller constants
	private static final double E_CONST = 0.5;
	private static final double ALPHA_CONST = 0.5;
	
	//controller representation
	private double[] w1, w2, w3, w4;
	private int numNeuronsPerLayer;
	private CodedPopulation xPop, yPop, aPop, bPop;
	private double[] a,b;
	
	public PopulationNetworkController(HammeringArm arm, Nail nail, int numNeuronsPerLayer) {
		this.arm = arm;
		this.nail = nail;
		this.numNeuronsPerLayer = numNeuronsPerLayer;
		resetController();
	}
	
	public void resetController() {
		w1 = new double[numNeuronsPerLayer];
		w2 = new double[numNeuronsPerLayer];
		w3 = new double[numNeuronsPerLayer];
		w4 = new double[numNeuronsPerLayer];
		xPop = new CodedPopulation("Amount Into Board Population", numNeuronsPerLayer, 10.0);
		yPop = new CodedPopulation("Amount Bent Population", numNeuronsPerLayer, 180.0);
		aPop = new CodedPopulation("Swing Theta population", numNeuronsPerLayer, 40.0);
		bPop = new CodedPopulation("Acceleration Population", numNeuronsPerLayer, 3.0);
		
		//set random weights initially
		for(int x=0; x < numNeuronsPerLayer; x++) {
			w1[x] = Math.random();
			w2[x] = Math.random();
			w3[x] = Math.random();
			w4[x] = Math.random();
		}		
	}
	
	public int[] selectAction(int x, int y) {		
		xPop.encodeValue(x);
		yPop.encodeValue(y);
		a = compute_a(x,y);
		b = compute_b(x,y);
				
		aPop.setFiringRate(a);
		bPop.setFiringRate(b);
		
		double a_decoded = aPop.decodeValue_CenterOfMass();
		double b_decoded = bPop.decodeValue_CenterOfMass();
		
		int[] action = new int[2];
		action[0] = (int)a_decoded;
		action[1] = (int)b_decoded;
		
		System.out.println(action[0] + " " + action[1]);
		
		return action;
	}
	
	public void updateRepresentation(VisualResult vr, int[] action, double reward) {
		double r = reward;
		for(int i=0; i < numNeuronsPerLayer; i++) {
			w1[i] = E_CONST*(r*xPop.getFiringRate()[i]*a[i]) /* + ALPHA_CONST*(-1*w1[i])*/;
			w2[i] = E_CONST*(r*yPop.getFiringRate()[i]*a[i]) /*+ ALPHA_CONST*(-1*w2[i])*/;
			w3[i] = E_CONST*(r*xPop.getFiringRate()[i]*b[i]) /*+ ALPHA_CONST*(-1*w3[i])*/;
			w4[i] = E_CONST*(r+yPop.getFiringRate()[i]*b[i]) /*+ ALPHA_CONST*(-1*w4[i])*/;
		}
	}
	
	public double[] compute_a(double x, double y) {
		double[] rtn = new double[numNeuronsPerLayer];
		for(int z=0; z < numNeuronsPerLayer; z++) {
			rtn[z] = x*w1[z] + y*w2[z];
		}
		
		return rtn;
	}
	
	public double[] compute_b(double x, double y) {
		double[] rtn = new double[numNeuronsPerLayer];
		for(int z=0; z < numNeuronsPerLayer; z++) {
			rtn[z] = x*w3[z] + y*w4[z];
		}
		
		return rtn;
	}
	
	public void endDebug(){
		
		//add pop code points
		if(controllerView != null) {
			controllerView.addPopPoint(getNailNumber(), getNumActionsTaken());
		}		
	}	
}
