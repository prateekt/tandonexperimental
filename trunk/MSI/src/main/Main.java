package main;
import webcam.*;
import schemas.*;
import gui.*;
import schema_output.*;
import java.util.*;
import util.*;
import org.jfree.data.xy.*;
import org.jfree.chart.*;
import org.jfree.chart.util.*;
import org.jfree.chart.plot.PlotOrientation;
import java.io.*;

public class Main {

	public static void main(String[] args) {
		
		//Brain Schemas declared
		WebCam webcam = new MockWebCam("trials/prying/");
		Constants.getBrainSchemas().add(webcam);
//		RealWebCam webcam = new RealWebCam();
//		webcam.setRecord(true);
//		webcam.setRecordDirectory("trials/trial9/");
		GUI gui = new GUI(webcam);
		GUISchema guiSchema = new GUISchema(gui);
		VisualCortex vc = new VisualCortex();
		PrefrontalCortex pfc =  new PrefrontalCortex();
		PremotorCortex premotor = new PremotorCortex();
		ParietalCortex parietal = new ParietalCortex();
		List<ForwardModel> forwardModels = Constants.getForwardModels();
		DifferenceModule dm = new DifferenceModule();
		EstimatedMentalState em = new EstimatedMentalState();
		
		//Add to global schemas list
		Constants.getBrainSchemas().add(guiSchema);
		Constants.getBrainSchemas().add(vc);
		Constants.getBrainSchemas().add(pfc);
		Constants.getBrainSchemas().add(premotor);
		Constants.getBrainSchemas().add(parietal);
		Constants.getBrainSchemas().addAll(forwardModels);
		Constants.getBrainSchemas().add(dm);
		Constants.getBrainSchemas().add(em);
		
		//Connections
		webcam.setVisualCortex(vc);
		vc.setGUISchema(guiSchema);
		vc.setParietalCortex(parietal);
		parietal.setPremotorCortex(premotor);
		parietal.setDifferenceModule(dm);
		pfc.setParietalCortex(parietal);
		pfc.setPremotorCortex(premotor);
		premotor.setForwardModels(forwardModels);
		for(ForwardModel fm : forwardModels) {
			fm.setPremotorCortex(premotor);
			fm.setDifferenceModule(dm);
		}
		dm.setEstimatedMentalState(em);
		dm.setGuiSchema(guiSchema);
		em.setGUISchema(guiSchema);
		
		//start threads
		webcam.startThread();
		guiSchema.startThread();
		vc.startThread();
		parietal.startThread();
		pfc.startThread();
		premotor.startThread();
		for(ForwardModel fm : forwardModels) {
			fm.startThread();
		}
		dm.startThread();
		em.startThread();

		//send initial impulse for task parameter distribution
/*		try {
			Thread.sleep(100);
			pfc.sendInput(new EstimatedMentalStateOutput(null,null,-1));
		}
		catch(Exception e) {
			e.printStackTrace();
		}*/		
	}
}
