package behavior.follower.HMM;
import java.util.*;

import behavior.transcriber.base.*;

public class HMMImpl {
	
	private List<Melody> states;
	private List<Melody> endStates;
	private List<String> emissions;
	private double[][] tFunct;
	private double[] startProb;
	private double[][] eFunct;
	
	//CACHE FOR PROBLEMS
	private Map<String, Double> alphaCache = new HashMap<String, Double>();
	private Map<String, Double> vCache = new HashMap<String, Double>();
	
	public Melody computeL_viterbi(List<String> obs, String currentObs) {
		
		//init cache
		alphaCache = new HashMap<String, Double>();
		vCache = new HashMap<String, Double>();

		Melody rtn = null;
		double vMax = Double.NEGATIVE_INFINITY;
		for(int s=0; s < states.size(); s++) {
			String sState = states.get(s).compareStr();
			double currentV = computeVFunction_viterbi(obs, sState, currentObs);
			if(currentV > vMax) {
				vMax = currentV;
				rtn = states.get(s);
			}
		}		
		return rtn;		
	}
	
	public Melody computeL(List<String> obs, String currentObs) {
		
		//init cache
		alphaCache = new HashMap<String, Double>();
		vCache = new HashMap<String,Double>();

		Melody rtn = null;
		double vMax = Double.NEGATIVE_INFINITY;
		for(int s=0; s < states.size(); s++) {
			String sState = states.get(s).compareStr();
			double currentV = computeVFunction(obs, sState, currentObs);
			if(currentV > vMax) {
				vMax = currentV;
				rtn = states.get(s);
			}
		}		
		return rtn;
	}
	
	public double computeVFunction_viterbi(List<String> obs, String state, String currentObs) {
		
		//check cache
		if(vCache.containsKey(state + " " + currentObs)) {
			return vCache.get(state + " " + currentObs);
		}
		
		double alpha = computeViterbiAlpha(obs, state, currentObs);
		
		//bottom sum
		double sum=0.0;
		for(int k=0; k < states.size(); k++) {
			String kState = states.get(k).compareStr();
			sum+= computeViterbiAlpha(obs, kState, currentObs);
		}
		
		if(sum==0)
			sum=1;
		
		double result = alpha / sum;
		
		//cache result
		vCache.put(state + " " + currentObs, result);
		
		//result result
		return result;
	}

	public double computeVFunction(List<String> obs, String state, String currentObs) {
		
		//check in cache for precomputed value
		if(vCache.containsKey(state + " " + currentObs)) {
			return vCache.get(state + " " + currentObs);
		}
		
		double alpha = computeAlphaFunction_revised(obs, state, currentObs);
		
		//bottom sum
		double sum=0.0;
		for(int k=0; k < states.size(); k++) {
			String kState = states.get(k).compareStr();
			sum+= computeAlphaFunction_revised(obs, kState, currentObs);
		}
		
		if(sum==0)
			sum=1;
		
		double result = alpha /sum;
		
		//cache result
		vCache.put(state + " " + currentObs, result);
		
		//return result
		return result;
	}
	
	public double computeViterbiAlpha(List<String> obs, String state, String currentObs) {
		
		//check in cache for precomputed value
		if(alphaCache.containsKey(state + " " + currentObs)) {
			return alphaCache.get(state + " " + currentObs);
		}
		
		int obsIndex = getObservationIndex(obs, currentObs);
		int s_j = getStateIndex(state);
		if(obsIndex==0) {
			
			double result = obsFunction(state, currentObs) * startProb[s_j];
			
			//cache result
			alphaCache.put(state + " " + currentObs, result);
			
			//return result
			return result;
		}
		else {
			double valMax= Double.NEGATIVE_INFINITY;
			for(int s_k=0; s_k < states.size(); s_k++) {
				String kState = states.get(s_k).compareStr();
				double currentVal =tFunct[s_k][s_j] * computeVFunction(obs,kState,obs.get(obsIndex-1));
				if(currentVal > valMax)
					valMax = currentVal;
			}
			double result= obsFunction(state,currentObs)*valMax;
			
			//cache result
			alphaCache.put(state + " " + currentObs, result);
			
			//return result
			return result;
		}		
	}
		
	public double computeAlphaFunction_revised(List<String> obs, String state, String currentObs) {
		
		//check in cache
		if(alphaCache.containsKey(state + " " + currentObs)) {
			return alphaCache.get(state + " "  +currentObs);
		}
		
		int obsIndex = getObservationIndex(obs, currentObs);
		int s_j = getStateIndex(state);
		if(obsIndex==0) {
			
			double result = obsFunction(state, currentObs) * startProb[s_j];
			
			//cache result
			alphaCache.put(state + " " +currentObs, result);
			
			//return result
			return result;
		}
		else {
			double sum=0.0;
			for(int s_k=0; s_k < states.size(); s_k++) {
				String kState = states.get(s_k).compareStr();
				sum+=tFunct[s_k][s_j] * computeVFunction(obs,kState,obs.get(obsIndex-1));
			}
			
			double result = obsFunction(state,currentObs)*sum;			
			
			//cache result
			alphaCache.put(state + " " + currentObs, result);
			
			//return result
			return result;
		}		
	}
	
	public double computeAlphaFunction(List<String> obs, String state, String currentObs) {
		int obsIndex = getObservationIndex(obs, currentObs);
		int s_j = getStateIndex(state);
		if(obsIndex==0) {
			return obsFunction(state, currentObs) * startProb[s_j];
		}
		else {
			double sum=0.0;
			for(int s_k=0; s_k < states.size(); s_k++) {
				String kState = states.get(s_k).compareStr();
				sum+=tFunct[s_k][s_j] * computeAlphaFunction(obs,kState,obs.get(obsIndex-1));
			}
			
			return obsFunction(state,currentObs)*sum;
		}		
	}
	
	public double obsFunction(String state, String emission) {
		int s_j = getStateIndex(state);
		int e_i = getEmissionIndex(emission);
				
		double p_s_j = 1.0/states.size();
		double p_e_i = 1.0/emissions.size();
		double k  = p_s_j / p_e_i;
		return k*eFunct[s_j][e_i];
	}
	
	public int getObservationIndex(List<String> obs, String current) {
		for(int x=0; x < obs.size(); x++) {
			String co = obs.get(x);
			if(current.equalsIgnoreCase(co))
				return x;
		}	
		return -1;
	}
	
	public int getEmissionIndex(String emission) {
		for(int x=0; x < emissions.size(); x++) {
			String ce = emissions.get(x);
			if(emission.equalsIgnoreCase(ce)) {
				return x;
			}
		}
		return -1;
	}
	
	public int getStateIndex(String state) {
		for(int x=0; x < states.size(); x++) {
			String cs = states.get(x).compareStr();
			if(cs.equalsIgnoreCase(state))
				return x;
		}
		return -1;
	}
	
	/**
	 * @return the states
	 */
	public List<Melody> getStates() {
		return states;
	}
	/**
	 * @param states the states to set
	 */
	public void setStates(List<Melody> states) {
		this.states = states;
	}
	/**
	 * @return the endStates
	 */
	public List<Melody> getEndStates() {
		return endStates;
	}
	/**
	 * @param endStates the endStates to set
	 */
	public void setEndStates(List<Melody> endStates) {
		this.endStates = endStates;
	}
	/**
	 * @return the emissions
	 */
	public List<String> getEmissions() {
		return emissions;
	}
	/**
	 * @param emissions the emissions to set
	 */
	public void setEmissions(List<String> emissions) {
		this.emissions = emissions;
	}
	/**
	 * @return the tFunct
	 */
	public double[][] gettFunct() {
		return tFunct;
	}
	/**
	 * @param tFunct the tFunct to set
	 */
	public void settFunct(double[][] tFunct) {
		this.tFunct = tFunct;
	}
	/**
	 * @return the startProb
	 */
	public double[] getStartProb() {
		return startProb;
	}
	/**
	 * @param startProb the startProb to set
	 */
	public void setStartProb(double[] startProb) {
		this.startProb = startProb;
	}
	/**
	 * @return the eFunct
	 */
	public double[][] geteFunct() {
		return eFunct;
	}
	/**
	 * @param eFunct the eFunct to set
	 */
	public void seteFunct(double[][] eFunct) {
		this.eFunct = eFunct;
	} 
}
