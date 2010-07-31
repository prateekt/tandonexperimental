package ui.automation;

import java.io.File;

import ui.BasicOperations;
import ui.UserOperations;
import util.db.DBManager;
import util.sys.SysUtil;
import weka.classifiers.Classifier;
import api.matlab.FFTCodeGenerator;
import api.matlab.MatlabControlUtil;
import api.weka.WekaUtil;

public class TestClassifier {
	
	private DBManager gtpDB;
	private DBManager uMidiDB;
	private DBManager uClipsDB;
	private DBManager tempDB;
	private DBManager clipsDB;
	private String gtpFile;
	
	public TestClassifier(String gtpFile) {
		gtpDB = new DBManager(UserOperations.GTP_DB_DIR, UserOperations.GTP_DB_DIR_EXT);
		uMidiDB = new DBManager(UserOperations.MID_UNLABELED_DIR, UserOperations.MID_DB_DIR_EXT);
		uClipsDB = new DBManager(UserOperations.CLIP_UNLABELED_DIR, UserOperations.CLIP_DB_DIR_EXT);
		tempDB = new DBManager(UserOperations.TEMP_DB_DIR, UserOperations.TEMP_DB_DIR_EXT);
		clipsDB = new DBManager(UserOperations.CLIP_DB_DIR, UserOperations.CLIP_DB_DIR_EXT);
		this.gtpFile = gtpFile;
	}
	
	public void test() {
		
		//initialize classifier
		Classifier J48Classifier = WekaUtil.unserializeClassifier(UserOperations.J48_SERIAL_FILE);
		Classifier NaiveBayesClassifier = WekaUtil.unserializeClassifier(UserOperations.BAYES_SERIAL_FILE);
		String label = gtpDB.getLabel(gtpFile);
		
		//generate midi training examples in db_unlabeled_midi
		BasicOperations.generateMidiTrainingExamples(gtpFile, gtpDB, uMidiDB, tempDB);

		//generate training examples in db_unlabeled_clips
		BasicOperations.generateWavTrainingExamples(label, uMidiDB, uClipsDB);
		
		//generate matlab code
		File[] files = uClipsDB.getDBFiles(label);
		FFTCodeGenerator.setDbDir(UserOperations.CLIP_UNLABELED_DIR);
		String code = FFTCodeGenerator.getCodeTemplate(files);
		SysUtil.writeFile(UserOperations.CODE_FILE, code);		
		MatlabControlUtil.runFile(UserOperations.CODE_FILE_COMMAND);
		
		//run weka procedure on db_unlabeled_clips
		for(File f : files) {
			String fileName = f.getName();
			BasicOperations.doWekaClassification(clipsDB, fileName, J48Classifier, NaiveBayesClassifier);
		}
	}
	
	public static void main(String[] args) {
		TestClassifier t = new TestClassifier("a1.gp4");
		t.test();
	}	
}
