package main;
import webcam.*;
import game.*;
import schemas.*;
import gui.*;
import schema_output.*;
import java.util.*;
import util.*;

public class Main {

	public static void main(String[] args) {
		
		//Brain Schemas declared and MSI init
		WebCam webcam = new MockWebCam("trials/trial2/");
		webcam.setOn(false);
//		RealWebCam webcam = new RealWebCam();
//		webcam.setRecord(true);
//		webcam.setRecordDirectory("trials/trial9/");
		VisualCortex vc = new VisualCortex();
		PrefrontalCortex pfc =  new PrefrontalCortex();
		PremotorCortex premotor = new PremotorCortex();
		ParietalCortex parietal = new ParietalCortex();
		List<ForwardModel> forwardModels = Constants.getForwardModels();
		DifferenceModule dm = new DifferenceModule();
		EstimatedMentalState em = new EstimatedMentalState();
		GUI gui = new GUI(webcam);
		GUISchema guiSchema = new GUISchema(gui);
		
		//init Game
		StoryMaster sm = new StoryMaster("scenes", gui);
		
		//Add to global schemas list
		Constants.getBrainSchemas().add(webcam);
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
		sm.setEstimatedMentalState(em);
		
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
		sm.startThread();

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
