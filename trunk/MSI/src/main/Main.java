package main;
import webcam.*;
import schemas.*;
import gui.*;
import schema_output.*;
import java.util.*;
import util.*;

public class Main {

	public static void main(String[] args) {
		
		//Brain Schemas declared
		WebCam webcam = new WebCam();
		GUI gui = new GUI(webcam);
		GUISchema guiSchema = new GUISchema(gui);
		VisualCortex vc = new VisualCortex();
		PrefrontalCortex pfc =  new PrefrontalCortex();
		PremotorCortex premotor = new PremotorCortex();
		ParietalCortex parietal = new ParietalCortex();
		List<ForwardModel> forwardModels = Constants.getForwardModels();
		DifferenceModule dm = new DifferenceModule();
		EstimatedMentalState em = new EstimatedMentalState();
		
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
		try {
			Thread.sleep(2000);
			pfc.sendInput(new EstimatedMentalStateOutput(null,null));
		}
		catch(Exception e) {
			e.printStackTrace();
		}		
	}
	
/*	public static void main(String[] args) {		
		//Brain Schemas declared
		PremotorCortex premotor = new PremotorCortex();
		List<ForwardModel> forwardModels = Constants.getForwardModels();
		DifferenceModule dm = new DifferenceModule();
		EstimatedMentalState em = new EstimatedMentalState();
		
		//connections
		premotor.setForwardModels(forwardModels);
		for(ForwardModel fm : forwardModels) {
			fm.setPremotorCortex(premotor);
			fm.setDifferenceModule(dm);
		}
		dm.setEstimatedMentalState(em);
		
		//start threads
		premotor.startThread();
		for(ForwardModel fm : forwardModels) {
			fm.startThread();
		}
		dm.startThread();
		em.startThread();
		
		try {
			//send stimulus to premotor cortex to init fms
			premotor.sendPFCInput(new PrefrontalCortexOutput(1));

			//send premotor cortex initial control variables
			premotor.sendPCInput(new ParietalCortexOutput(10,Math.PI+70));

			//send difference module some data from parietal
			Thread.sleep(5000);
			dm.sendPCInput(new ParietalCortexOutput(10, Math.PI+80));
			Thread.sleep(5000);
			dm.sendPCInput(new ParietalCortexOutput(6, Math.PI+90));
			Thread.sleep(5000);			
			dm.sendPCInput(new ParietalCortexOutput(1000, Math.PI+180));
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	} */

/*	public static void main(String[] args) {
		//Brain Schemas declared
		WebCam webcam = new WebCam();
		GUI gui = new GUI(webcam);
		GUISchema guiSchema = new GUISchema(gui);
		VisualCortex vc = new VisualCortex();
		PrefrontalCortex pfc =  new PrefrontalCortex();
		PremotorCortex premotor = new PremotorCortex();
		ParietalCortex parietal = new ParietalCortex();
		
		//connections
		webcam.setVisualCortex(vc);
		vc.setGUISchema(guiSchema);
		vc.setParietalCortex(parietal);
		parietal.setPremotorCortex(premotor);
		pfc.setParietalCortex(parietal);
		pfc.setPremotorCortex(premotor);
		
		//start threads
		webcam.startThread();
		guiSchema.startThread();
		vc.startThread();
		parietal.startThread();
		pfc.startThread();
		premotor.startThread();
		
		//send initial impulse for task parameter distribution
		try {
			Thread.sleep(2000);
			pfc.sendInput(new EstimatedMentalStateOutput(null,null));
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}*/
}
