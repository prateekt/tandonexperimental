package src.controller.popcodectrl;

import src.env_model.HammeringArm;
import src.env_model.Nail;
import src.util.*;

public class PopulationNetworkController_WTA extends PopulationNetworkController_basic{
	
	//WE matrix constant
	protected double WE_MATRIX_CONSTANT = 0.7;

	//WI matrix constant
	protected double WI_MATRIX_CONSTANT = 2.5;
	
	//parameter for WE Matrix - choose so that excitations are one over.
	protected double sigma1Param;
	
	//parameter for WI Matrix - choose so little bigger than sigma1Param.
	protected double sigma2Param;
	
	protected Matrix we, wi;
	
	int iCtr =0;
	
	public PopulationNetworkController_WTA(HammeringArm arm, Nail nail, int numNeuronsPerLayer, double sigma1Param, double sigma2Param) {
		super(arm, nail, numNeuronsPerLayer);
		this.sigma1Param = sigma1Param;
		this.sigma2Param = sigma2Param;
		a = new double[numNeuronsPerLayer];
		b = new double[numNeuronsPerLayer];
		resetController2();
	}
	
	public void resetController2() {
		//do all stuff from before
//		super.resetController();
				
		//compute we and wi matrices
		double[][] we_temp = new double[numNeuronsPerLayer][numNeuronsPerLayer];
		double[][] wi_temp = new double[numNeuronsPerLayer][numNeuronsPerLayer];
		for(int i=0; i < numNeuronsPerLayer; i++) {
			for(int j=0; j < numNeuronsPerLayer; j++) {
				we_temp[i][j] = WE_MATRIX_CONSTANT*Math.exp( (-1*Math.pow(i-j,2))/(2*Math.pow(sigma1Param, 2)));
				wi_temp[i][j] = WI_MATRIX_CONSTANT - WI_MATRIX_CONSTANT*Math.exp((-1*Math.pow(i-j, 2))/(2*Math.pow(sigma2Param, 2)));
//				System.out.println(we_temp[i][j] + " " + wi_temp[i][j]);
			}
		}
		
		we = new Matrix(we_temp);
		wi = new Matrix(wi_temp);
	}

	public double[] compute_a(double x, double y) {
		double[] rtn = new double[numNeuronsPerLayer];
		
		//Compute matrix for a*we term
		double[][] aData = new double[1][numNeuronsPerLayer];
		for(int z=0; z < numNeuronsPerLayer; z++) {
			aData[0][z] = a[z];
		}
		Matrix mTerm = new Matrix(aData);
		Matrix aWEResultMatrix = mTerm.times(we);
		double[][] aWEResult = aWEResultMatrix.getData();
				
		//compute matrix for a*wi term
		Matrix aWIResultMatrix = mTerm.times(wi);
		double[][] aWIResult = aWIResultMatrix.getData();
		
		//put result together
		for(int z=0; z < numNeuronsPerLayer; z++) {
			rtn[z] = x*w1[z] + y*w2[z] + aWEResult[0][z] - aWIResult[0][z];
 		}
		
		return rtn;
	}
	
	public double[] compute_b(double x, double y) {

		double[] rtn = new double[numNeuronsPerLayer];
		
		//Compute matrix for b*we term
		double[][] bData = new double[1][numNeuronsPerLayer];
		for(int z=0; z < numNeuronsPerLayer; z++) {
			bData[0][z] = b[z];
		}
		Matrix bTerm = new Matrix(bData);
		Matrix bWEResultMatrix = bTerm.times(we);
		double[][] bWEResult = bWEResultMatrix.getData();
		
		//compute matrix for b*wi term
		Matrix bWIResultMatrix = bTerm.times(wi);
		double[][] aWIResult = bWIResultMatrix.getData();

		//put result together
		for(int z=0; z < numNeuronsPerLayer; z++) {
			rtn[z] = x*w3[z] + y*w4[z] + bWEResult[0][z] - aWIResult[0][z];
 		}		
		
		return rtn;
	}
}
