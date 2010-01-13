package src.rlcontroller;
import java.util.Random;

import src.model.*;

public class RLController {

	//Q LEARNING PARAMS
	private static final int NUM_STATES = 3;
	private static final int NUM_SWING_PARAMS = 120;
	private static final int NUM_ACCEL_PARAMS = 3;	
	private static final double FAILURE_COST = 50;
	private static final double A_CONST = 0.5;
	private static final double B_CONST = 0.5;
	private static final double DISCOUNT_FACTOR = 0.9;
	private double[][][] qTable;
	private double exploreProbability;
	
	//Nail
	private Nail nail;
	private HammeringArm arm;
	
	public RLController(Nail nail, HammeringArm arm) {
		qTable = new double[NUM_STATES][NUM_SWING_PARAMS][NUM_ACCEL_PARAMS];
		this.nail = nail;
		this.arm = arm;
		exploreProbability = 0.5;
	}
	
	public void qlearning(int steps) {
		
		//initialize all table entries to 0
		for(int x=0; x < qTable.length; x++) {
			for(int y=0; y < qTable[x].length; y++) {
				for(int z=0; z < qTable[x][y].length; z++)
				qTable[x][y][z] = 0.0;
			}
		}

		//observe current state
		int currentState = getEnvState();

		//do forever
		outer:for(int t=0; t < steps; t++) {
			
			//select an action and execute it
			int[] action = selectAction(currentState);
			VisualResult vr = executeAction(action);
			
			//receive reward r
			double reward = getReward(vr);
			
			//observe new state s
			int newState = getEnvState();
			
			if(newState==1) {
				System.out.println("NAILING DONE: " + t);
				break outer;
			}
			if(newState==2) {
				System.out.println("NAIL BENT: " + t);
				break outer;
			}
			
			//iterate through new state to see best can do from that state
			int bestY=0, bestZ=0;
			for(int y=0; y < qTable[newState].length; y++) {
				for(int z=0; z < qTable[newState][y].length; z++) {
					if(qTable[newState][y][z] > qTable[newState][bestY][bestZ]) {
						bestY = y;
						bestZ = z;
					}
				}
			}
			
			//update table
			qTable[currentState][action[0]][action[1]] = reward + DISCOUNT_FACTOR*qTable[newState][bestY][bestZ];
			
			//s=s'
			currentState = newState;
		}
	}
	
	public double getReward(VisualResult vr) {
		if(vr.isNailBent()) {
			return FAILURE_COST;
		}
		else {
			return A_CONST*vr.getCurrentInAmount() - B_CONST*vr.getCurrentBentAmount();
		}
	}
	
	public VisualResult executeAction(int[] action) {
		return arm.executeSwing(action[0], action[1]+1);		
	}
	
	public int[] selectAction(int state) {
		
		int[] rtn = new int[2];
		
		//prob
		double rand = Math.random();
		if(rand > exploreProbability) {
			
			//explore
			Random r = new Random();
			int exploreSwing = r.nextInt(NUM_SWING_PARAMS);  
			int exploreAccel = r.nextInt(NUM_ACCEL_PARAMS);
			rtn[0] = exploreSwing;
			rtn[1] = exploreAccel;
			return rtn;
		}
		else {			
			
			//exploit best current action from state
			int yBest=0, zBest=0;
			for(int y=0; y < qTable[state].length; y++) {
				for(int z=0; z < qTable[state][y].length; z++) {
					if(qTable[state][y][z] > qTable[state][yBest][zBest]) {
						yBest = y;
						zBest = z;
					}
				}
			}
			rtn[0] = yBest;
			rtn[1] = zBest;			
			return rtn;
		}
	}
	
	public int getEnvState() {

		//state 0 - HIT_STATE
		//state 1 - NAILING_COMPLETE
		//state 2 - NAIL_BENT
		//nail in takes precedence over nail bent
		
		if(nail.nailIn()) {
			return 1;
		}
		if(nail.nailBent()) {
			return 2;
		}
		
		return 0;
	}
}