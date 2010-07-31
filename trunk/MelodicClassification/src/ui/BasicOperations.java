package ui;

import java.io.File;
import java.util.List;

import api.weka.FeatureSet;
import api.weka.FeatureTable;
import api.weka.WekaUtil;

import util.db.DBManager;
import util.db.GTPDBToMidDB;
import util.db.MidDBToClipDB;
import util.file.FileDataSet;
import util.sys.SysUtil;
import weka.classifiers.Classifier;

public class BasicOperations {

	public static void generateMidiTrainingExamples(String fileName, DBManager gtpDB, DBManager midDB, DBManager tempDB) {
		if(!gtpDB.containsFile(fileName)) {
			System.out.println("Midi Training Example Generatation Failed - Root File found found.\n");			
		}
		else {
			//generate examples using converter
			GTPDBToMidDB gmConverter = new GTPDBToMidDB(gtpDB, midDB, tempDB);
			File f = gtpDB.getFile(fileName);
			gmConverter.convert(f);
			System.out.println("Training examples generated.\n");			
		}
	}
	
	public static void generateWavTrainingExamples(String label, DBManager midDB, DBManager clipDB) {

		if(!midDB.containsLabel(label)) {
			System.out.println("Export Failed -- Label not found.\n");
		}
		else {
			
			MidDBToClipDB mcConverter = new MidDBToClipDB(midDB,clipDB);
			mcConverter.handleConversion(label);
			System.out.println("Wavs generated successfully.\n");
		}
	}
	
	public static void doWekaClassification(DBManager clipDB, String fileName, Classifier J48Classifier, Classifier NaiveBayesClassifier) {		
		//obtain possible series
		List<String> possibleSeries = clipDB.obtainSeries();
		String[] possibleSeriesNames = new String[possibleSeries.size()];
		for(int x=0; x < possibleSeries.size(); x++) {
			possibleSeriesNames[x] = possibleSeries.get(x);
		}
		
		//load file feature vector
		String fileNameMini = fileName.substring(0, fileName.lastIndexOf("."));
		FileDataSet fd = new FileDataSet(fileNameMini, possibleSeriesNames[0], possibleSeriesNames,UserOperations.CLIP_UNLABELED_DIR);
		FeatureSet fs = fd.computeFeatureSet();
		FeatureTable ft = new FeatureTable();
		ft.addFeatureSet(fs);
		
		//write to file
		String wekaFile = ft.genWekaTable("AUDIO_CLASSIFICATION");
		SysUtil.writeFile(UserOperations.ARFF_FILE, wekaFile);

		//plug into weka and get label
		System.out.println("-----------J48 Output-----------");
		WekaUtil.classify(UserOperations.ARFF_FILE, J48Classifier);
		System.out.println("-----------J48 Output-----------");
		System.out.println("----------Bayes Output----------");
		WekaUtil.classify(UserOperations.ARFF_FILE, NaiveBayesClassifier);
		System.out.println("----------Bayes Output----------\n");		
	}	
}
