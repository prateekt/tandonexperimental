package util.db;

import java.io.File;

import util.sys.SysUtil;

import api.matlab.MatlabControlUtil;
import api.matlab.Midi2WavCodeGenerator;
import java.util.*;

public class MidDBToClipDB extends DBConverter{
	
	private static final String MATLAB_CONVERSION_FILE = "convert.m";
	private static final String MATLAB_CONVERSION_COMMAND = "convert";
	private static final double MATLAB_FREQ_SCALE = 1.0;
	
	public MidDBToClipDB(DBManager midDB, DBManager clipDB) {
		super(midDB, clipDB);
	}
	
	public void handleConversion(String label) {
		SysUtil.writeFile(MATLAB_CONVERSION_FILE, getMatLabCode(label));
		MatlabControlUtil.runFile(MATLAB_CONVERSION_COMMAND);		
	}
	
	@Override
	public void handleConversion() {
		SysUtil.writeFile(MATLAB_CONVERSION_FILE, getMatLabCode());
		MatlabControlUtil.runFile(MATLAB_CONVERSION_COMMAND);
	}
	
	public String getMatLabCode(String label) {
		File[] files = db1.getDBFiles(label);
		StringBuffer rtn = new StringBuffer();
		int index = db2.getNextIndex(label);
		for(File file : files) {
			
			//write code
			String outputFile = db2.computeFilePath(label, index);
			String code = getMatLabCode(file.getAbsolutePath(), outputFile);
			rtn.append(code);
			
			//update index
			index++;
		}
		
		return rtn.toString();
	}
	
	public String getMatLabCode() {
		File[] files = db1.getDBFiles();
		StringBuffer rtn = new StringBuffer();
		
		Map<String, Integer> labelsToIndex = new HashMap<String,Integer>();
		for(File file : files) {
			String label = db1.getLabel(file.getName());

			//obtain index
			int index=-1;
			if(labelsToIndex.containsKey(label)) {
				index = labelsToIndex.get(label);			
			}
			else {
				index = db2.getNextIndex(label);
			}
			
			//write code
			String outputFile = db2.computeFilePath(label, index);
			String code = getMatLabCode(file.getAbsolutePath(), outputFile);
			rtn.append(code);
			
			//update map
			labelsToIndex.put(label, index+1);
		}
		
		return rtn.toString();		
	}
	
	public String getMatLabCode(String inputFile, String outputFile) {
		String code = Midi2WavCodeGenerator.single_file_handler(inputFile, outputFile, MATLAB_FREQ_SCALE);
		return code;
	}
}