package util;
import java.util.*;
import webcam.*;
import schemas.*;
import schema_output.*;

/**
 * File of simulation constants
 * @author Prateek Tandon
 *
 */
public class Constants {
	
	/**
	 * The clock for the camera. How quickly are
	 * images sampled.
	 */
	public static final int CLOCK =  500;
	
	/**
	 * The number of time steps the simulation should
	 * run for.
	 */
	public static final int NUMBER_TIME_STEPS = 80;
	
	/**
	 * Holding demo directory
	 */
	public static final String HOLDING_DEMO_DIR = "trials/holding/";
	
	/**
	 * Nailing demo directory
	 */
	public static final String NAILING_DEMO_DIR = "trials/nailing/";
	
	/**
	 * Prying demo directory
	 */
	public static final  String PRYING_DEMO_DIR = "trials/prying/";
	
	/**
	 * List of forward models in the simulation. Easily
	 * extensible to add more forward models.
	 */
	private static List<ForwardModel> fmList;
	
	/**
	 * Listing of all brain schemas in simulation.
	 */
	private static List<BrainSchema> schemasList;
		
	/**
	 * Gets forward models in simulation
	 * @return forward models
	 */
	public static List<ForwardModel> getForwardModels() {
		if(fmList==null) {
			fmList = new ArrayList<ForwardModel>();
			fmList.add(new HoldingForwardModel());
			fmList.add(new NailingForwardModel());
			fmList.add(new PryingForwardModel());
		}		
		return fmList;
	}
	
	public static List<BrainSchema> getBrainSchemas()  {
		if(schemasList==null) {
			schemasList = new ArrayList<BrainSchema>();
		}
		return schemasList;
	}
	
	public static void resetSchemas() {
		for(BrainSchema b : getBrainSchemas()) {
			b.sendReset("RESET");
		}
	}
	
	public static void restartSchemas() {
		resetSchemas();

		//find key schemas
		PrefrontalCortex pfc = null;
		WebCam webcam = null;
		ParietalCortex pc = null;
		for(BrainSchema b : getBrainSchemas()) {
			if(b instanceof PrefrontalCortex)
				pfc = (PrefrontalCortex) b;
			if(b instanceof WebCam)
				webcam = (WebCam) b;
			if(b instanceof ParietalCortex)
				pc = (ParietalCortex) b;
		}
		
		if(pfc!=null && webcam!=null) {
		
			//tell webcam to go off
			webcam.setOn(true);
			
			//send pfc init signal
			pfc.sendInput(new EstimatedMentalStateOutput(null,null,-1));			
		}		
	}
	
	public static WebCam getWebCam() {
		for(BrainSchema b : getBrainSchemas()){
			if(b instanceof WebCam)
				return (WebCam) b;
		}
		return null;
	}
}
