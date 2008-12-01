package gui;
import schemas.*;
import schema_output.*;
import  java.util.*;
import java.util.concurrent.*;

public class GUISchema extends BrainSchema {
	
	private GUI gui	;
	private Queue<VisualCortexOutput> visInput;
	private Queue<ControlVariableSummary> fmOutputs;
	
	
	public GUISchema(GUI gui) {
		super("GUI Schema");
		this.gui = gui;
		visInput = new ConcurrentLinkedQueue<VisualCortexOutput>();
		fmOutputs = new ConcurrentLinkedQueue<ControlVariableSummary>();
	}
	
	public void sendVisInput(VisualCortexOutput input) {
//		this.printDebug("Received tagged image");
		visInput.add(input);
		receivedInput();
	}
	
	public void sendFMInput(ControlVariableSummary cv) {
		this.printDebug("recieved cv");
		fmOutputs.add(cv);
		receivedInput();
	}
	
	public boolean produceOutput() {
		if(visInput.size() > 0) {
			VisualCortexOutput input = visInput.remove();
			gui.setVisualInput(input);
			return true;
		}
		if(fmOutputs.size() > 0) {
			this.printDebug("Printing");
			ControlVariableSummary cv = fmOutputs.remove();
			gui.cvPanel.setControlVariableSummary(cv);
			gui.cvPanel.repaint();
			gui.cvPanel.validate();
		}
		return false;
	}
}
