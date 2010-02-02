package src.controller;

import src.env_model.*;
import src.gui.*;

public abstract class Controller {
	
	//reward function constants
	private static final double FAILURE_COST = 50;
	private static final double A_CONST = 0.5;
	private static final double B_CONST = 0.5;
	
	//objects
	protected HammeringArm arm;
	protected Nail nail;
	
	//debug
	private int numActionsTaken;
	private int nailNumber;
	
	//controller view
	protected ControllerView controllerView;
	
	public void control(int steps) {

		//reset numActions
		numActionsTaken = 0;
		
		//observe current state
		double amountIntoBoard = nail.getAmountIntoBoard();
		double amountBent = nail.getAmountBent();

		//do forever
		outer:for(int t=0; t < steps; t++) {
			
			try {
				Thread.sleep(1000);
			}
			catch(Exception e) {
				e.printStackTrace();
			}
			
			//select an action and execute it
			int[] action = selectAction((int)amountIntoBoard, (int)amountBent);
			VisualResult vr = executeAction(action);
			numActionsTaken++;
			
			//receive reward r
			double reward = getReward(vr);
			
			//observe new state s
			int newState = nail.getNailState(); 
			if(newState==1 || newState==2) {
				break outer;
			}
			
			//update representation
			updateRepresentation(vr, action, reward);
						
			//s=s'
			amountIntoBoard = vr.getCurrentInAmount();
			amountBent = vr.getCurrentBentAmount();			
		}
		
		endDebug();
		
		//reset environment
		numActionsTaken=0;
		nail.reset();
		arm.reset();		
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
	
	public abstract int[] selectAction(int amountIntoBoard, int amountBent);
	public abstract void updateRepresentation(VisualResult vr, int[] action, double reward);
	public abstract void endDebug();

	public int getNumActionsTaken() {
		return numActionsTaken;
	}

	public ControllerView getControllerView() {
		return controllerView;
	}

	public void setControllerView(ControllerView controllerView) {
		this.controllerView = controllerView;
	}

	public int getNailNumber() {
		return nailNumber;
	}

	public void setNailNumber(int nailNumber) {
		this.nailNumber = nailNumber;
	}
}