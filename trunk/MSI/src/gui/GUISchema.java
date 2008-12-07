package gui;
import schemas.*;
import schema_output.*;
import  java.util.*;
import java.util.concurrent.*;

/**
 * The gui schema that asychronously updates gui. Runs it its own thread
 * and receives messages from brain parts when it is ready to update
 * parts of the gui.
 * @author Prateek Tandon
 *
 */
public class GUISchema extends BrainSchema {
	
	/**
	 * The gui object this schema controls.
	 */
	private GUI gui	;
	
	/**
	 * Received updates from the visual cortex.
	 */
	private Queue<VisualCortexOutput> visInput;

	/**
	 * Received updates from the difference module.
	 */
	private Queue<DifferenceModuleOutput> dmInput;

	/**
	 * Received updates from the estimated mental state schema.
	 */
	private Queue<EstimatedMentalStateOutput> emInput;

	/**
	 * Received control variable updates from the difference module. 
	 */
	private Queue<ControlVariablesUpdate> dmVarInput;
	
	/**
	 * Constrcutor
	 * @param gui The gui object this schema controls.
	 */
	public GUISchema(GUI gui) {
		super("GUI Schema");
		this.gui = gui;
		visInput = new ConcurrentLinkedQueue<VisualCortexOutput>();
		dmInput = new ConcurrentLinkedQueue<DifferenceModuleOutput>();
		emInput = new ConcurrentLinkedQueue<EstimatedMentalStateOutput>();
		dmVarInput = new ConcurrentLinkedQueue<ControlVariablesUpdate>();
	}
	
	/**
	 * Called by the visual cortex to send input to the gui schema.
	 * @param input The signal from the visual cortex
	 */
	public void sendVisInput(VisualCortexOutput input) {
//		this.printDebug("Received tagged image");
		visInput.add(input);
		receivedInput();
	}
	
	/**
	 * Called by the difference module to send input to the gui schema.
	 * @param input The signal from the difference module schema
	 */
	public void sendDMInput(DifferenceModuleOutput input) {
//		this.printDebug("Recieved dm");
		dmInput.add(input);
		receivedInput();
	}

	/**
	 * Called by the estimated mental state schema to send input to the gui schema.
	 * @param input  The signal from the estimated mental state module
	 */
	public void sendEMInput(EstimatedMentalStateOutput input) {
//		this.printDebug("Recieved em");
		emInput.add(input);
		receivedInput();
	}
	
	/**
	 * Called by the difference module to send input to the gui schema.
	 * @param realDOC The observed distance from center update
	 * @param realOA  The observed angular orientation update
	 * @param simDOC The simulated distance from center at this time step
	 * @param simOA The simulated angular orientation at this time step
	 * @param timeStep The time step
	 */
	public void sendDMCurrentVars(double realDOC, double realOA, Map<String, Double> simDOC, Map<String, Double> simOA, int timeStep) {
		ControlVariablesUpdate update = new ControlVariablesUpdate(realDOC, realOA, simDOC, simOA, timeStep);
		dmVarInput.add(update);
		receivedInput();
	}
	
	/**
	 * Updates gui with received inputs.
	 */
	public boolean produceOutput() {
		if(resetSignals.size() > 0) {
			resetSignals.clear();
			visInput.clear();
			dmInput.clear();
			emInput.clear();
			dmVarInput.clear();
		}
		
		if(visInput.size() > 0) {
			VisualCortexOutput input = visInput.remove();
			gui.setVisualInput(input);
		}
		if(dmInput.size() > 0) {
			DifferenceModuleOutput input = dmInput.remove();
			List<FMDDPair> pairs = input.getPairs();
			for(FMDDPair pair : pairs) {
				if(pair.getForwardModel().equalsIgnoreCase("Holding Forward Model")) {
					gui.getDDPanel().addHoldingSeriesPoint(input.getTimeStep(), pair.getDiscountedDifference());
				}
				if(pair.getForwardModel().equalsIgnoreCase("Nailing Forward Model")) {
					gui.getDDPanel().addNailingSeriesPoint(input.getTimeStep(), pair.getDiscountedDifference());
				}
				if(pair.getForwardModel().equalsIgnoreCase("Prying Forward Model")) {
					gui.getDDPanel().addPryingSeriesPoint(input.getTimeStep(), pair.getDiscountedDifference());
				}
			}
		}
		if(emInput.size() > 0) {
			EstimatedMentalStateOutput input = emInput.remove();
			Map<String, Double> fmToProb = input.getFmToProb();
			for(String fm : fmToProb.keySet()) {
				if(fm.equalsIgnoreCase("Holding Forward Model")) {
					gui.getProbPanel().addHoldingSeriesPoint(input.getTimeStep(), fmToProb.get(fm));
				}
				if(fm.equalsIgnoreCase("Nailing Forward Model")) {
					gui.getProbPanel().addNailingSeriesPoint(input.getTimeStep(), fmToProb.get(fm));
				}
				if(fm.equalsIgnoreCase("Prying Forward Model")) {
					gui.getProbPanel().addPryingSeriesPoint(input.getTimeStep(), fmToProb.get(fm));
				}
			}
		}
		if(dmVarInput.size() > 0) {
			ControlVariablesUpdate input = dmVarInput.remove();
		
			for(String fm : input.getSimDOC().keySet()) {
				if(fm.equalsIgnoreCase("Holding Forward Model")) {
					gui.getDOCPanel().addHoldingSeriesPoint(input.getTimeStep(), input.getSimDOC().get(fm));
					gui.getOAPanel().addHoldingSeriesPoint(input.getTimeStep(), input.getSimOA().get(fm));
				}
				if(fm.equalsIgnoreCase("Nailing Forward Model")) {
					gui.getDOCPanel().addNailingSeriesPoint(input.getTimeStep(), input.getSimDOC().get(fm));
					gui.getOAPanel().addNailingSeriesPoint(input.getTimeStep(), input.getSimOA().get(fm));
				}
				if(fm.equalsIgnoreCase("Prying Forward Model")) {
					gui.getDOCPanel().addPryingSeriesPoint(input.getTimeStep(), input.getSimDOC().get(fm));
					gui.getOAPanel().addPryingSeriesPoint(input.getTimeStep(), input.getSimOA().get(fm));
				}
				
				gui.getDOCPanel().addObservedSeriesPoint(input.getTimeStep(), input.getRealDOC());
				gui.getOAPanel().addObservedSeriesPoint(input.getTimeStep(), input.getRealOA());
			}
		}
		return true;
	}
}

/**
 * Struct for control variable updates.
 * @author Prateek Tandon
 *
 */
class ControlVariablesUpdate {
	
	/**
	 * The observed distance of center
	 */
	private double realDOC;

	/**
	 * The observed orientation difference
	 */
	private double realOA;
	
	/**
	 * The simulated distance of center
	 */
	private Map<String, Double> simDOC = null;
	
	/**
	 * The simulated orientation difference
	 */
	private Map<String, Double> simOA = null;

	/**
	 * The current time step
	 */
	private int timeStep = -1;
	
	public ControlVariablesUpdate(double realDOC, double realOA, Map<String, Double> simDOC, Map<String, Double> simOA, int timeStep) {
		this.realDOC = realDOC;
		this.realOA = realOA;
		this.simDOC = simDOC;
		this.simOA = simOA;
		this.timeStep = timeStep;
	}

	/**
	 * @return the realDOC
	 */
	public double getRealDOC() {
		return realDOC;
	}

	/**
	 * @param realDOC the realDOC to set
	 */
	public void setRealDOC(double realDOC) {
		this.realDOC = realDOC;
	}

	/**
	 * @return the realOA
	 */
	public double getRealOA() {
		return realOA;
	}

	/**
	 * @param realOA the realOA to set
	 */
	public void setRealOA(double realOA) {
		this.realOA = realOA;
	}

	/**
	 * @return the simDOC
	 */
	public Map<String, Double> getSimDOC() {
		return simDOC;
	}

	/**
	 * @param simDOC the simDOC to set
	 */
	public void setSimDOC(Map<String, Double> simDOC) {
		this.simDOC = simDOC;
	}

	/**
	 * @return the simOA
	 */
	public Map<String, Double> getSimOA() {
		return simOA;
	}

	/**
	 * @param simOA the simOA to set
	 */
	public void setSimOA(Map<String, Double> simOA) {
		this.simOA = simOA;
	}

	/**
	 * @return the timeStep
	 */
	public int getTimeStep() {
		return timeStep;
	}

	/**
	 * @param timeStep the timeStep to set
	 */
	public void setTimeStep(int timeStep) {
		this.timeStep = timeStep;
	}
}
