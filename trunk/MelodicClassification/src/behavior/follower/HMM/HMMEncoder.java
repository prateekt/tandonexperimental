package behavior.follower.HMM;

import java.util.HashMap;
import java.util.Map;

import behavior.transcriber.base.Melody;
import behavior.transcriber.base.Note;
import java.util.*;

public class HMMEncoder {
	
	public static HMMImpl encode(List<Melody> melSeq) {
		HMMImpl rtn = new HMMImpl();
		
		//states
		rtn.setStates(melSeq);
		
		//end states
		List<Melody> endStates = getEndStates(melSeq);
		rtn.setEndStates(endStates);
		
		//emissions		
		List<String> emissions = getEmissions(melSeq);
		rtn.setEmissions(emissions);
		
		//state transition function
		double[][] tFunct = getTransitionFunction(melSeq);
		rtn.settFunct(tFunct);
		
		//start state prob
		double[] startProb = getStartStateProb(melSeq);
		rtn.setStartProb(startProb);
		
		//emission function - p(sj,ei) ==> P(sj will emit ei)
		double[][] eTable = getEmissionFunction(melSeq, emissions, rtn);
		rtn.seteFunct(eTable);
		
		return rtn;
	}
	
	public static double[][] getEmissionFunction(List<Melody> melSeq, List<String> emissions, HMMImpl current) {
		double[][] rtn = new double[melSeq.size()][emissions.size()];
		
		for(int x=0; x < melSeq.size(); x++) {
			Melody mellie = melSeq.get(x);
			Map<String, Double> eTable = getEmissionProbability(mellie);
			for(String emission : eTable.keySet()) {
				int eIndex = current.getEmissionIndex(emission);
				rtn[x][eIndex] = eTable.get(emission);
			}
		}
		
		return rtn;
	}
		
	public static double[] getStartStateProb(List<Melody> melSeq) {
		double[] rtn = new double[melSeq.size()];
		for(int x=0; x < rtn.length; x++) { 
			rtn[x] = 1.0 / melSeq.size(); //all start states equiprob
		}
		return rtn;
	}
	
	public static double[][] getTransitionFunction(List<Melody> melSeq) {		
		double[][] rtn = new double[melSeq.size()][melSeq.size()]; //start, end
		
		for(int x=0; x < melSeq.size(); x++) {
			
			//markov
			if(x!=melSeq.size()-1)
				rtn[x][x+1] = 1.0;
		}
		
		return rtn;
	}
	
	public static List<Melody> getEndStates(List<Melody> melSeq) {
		List<Melody> rtn = new ArrayList<Melody>();
		rtn.add(melSeq.get(melSeq.size()-1));
		return rtn;
	}
	
	public static List<String> getEmissions(List<Melody> melSeq) {
		List<String> rtn = new ArrayList<String>();
		for(Melody m : melSeq) {
			for(Note n : m.getNotes()) {
				if(!rtn.contains(n.getName())) {
					rtn.add(n.getName());
				}
			}
		}
		return rtn;
	}
	
	public static Map<String, Double> getEmissionProbability(Melody m) {
		Map<String, Double> rtn = new HashMap<String,Double>();
		
		//generate count table
		int numElem=0;
		for(Note note : m.getNotes()) {
			if(!rtn.containsKey(note.getName())) {
				rtn.put(note.getName(), 1.0);
			}
			else {
				rtn.put(note.getName(), rtn.get(note.getName())+1);
			}
			numElem++;
		}
		
		//turn into probability table
		for(String key : rtn.keySet()) {
			double val = rtn.get(key);
			val = val / numElem;
			rtn.put(key, val);
		}
		
		return rtn;
	}	
}