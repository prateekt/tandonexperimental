package util;
import java.util.*;

import schemas.*;

public class Constants {
	
	public static final int CLOCK =  1000;
	
	public static final int NUMBER_TIME_STEPS = 1000;
	
	private static List<ForwardModel> fmList;
	
	public static List<ForwardModel> getForwardModels() {
		if(fmList==null) {
			fmList = new ArrayList<ForwardModel>();
			fmList.add(new HoldingForwardModel());
			fmList.add(new NailingForwardModel());
			fmList.add(new PryingForwardModel());
		}		
		return fmList;
	}
}
