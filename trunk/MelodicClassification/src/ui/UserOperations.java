package ui;

import java.util.*;
import java.io.*;

import behavior.classifier.DistanceBasedClassifier;
import behavior.classifier.ScaleInvariantClassifier;
import behavior.learning.MelodyLearner.MelodyLearningTask;
import behavior.transcriber.base.Melody;
import behavior.transcriber.base.Note;
import behavior.transcriber.chroma_based.ChromaBasedTranscription;
import behavior.transcriber.chroma_based.ChromaNoteTranscriber;




import api.jfugue.JFugueUtil;
import api.jfugue.MelodyToJFugue;
import api.matlab.*;
import api.soundrecoder.*;
import api.tuxguitar.MelodyExtractor.TGMelodyExtractor;
import api.weka.*;

import util.db.DBManager;
import util.db.GTPDBToMidDB;
import util.db.MidDBToClipDB;
import util.file.ExtFilter;
import util.file.FileDataSet;
import util.math.StringMath;
import util.sys.*;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
	
public class UserOperations {
	
	public static final String MASTER_DB_DIR = "db";
	public static final String CLIP_DB_DIR = MASTER_DB_DIR + "/" + "db_clips";
	public static final String MID_DB_DIR = MASTER_DB_DIR + "/" + "db_mid";
	public static final String GTP_DB_DIR = MASTER_DB_DIR + "/" + "db_gtp";
	public static final String TEMP_DB_DIR = MASTER_DB_DIR + "/" + "db_temp";
	public static final String CLIP_UNLABELED_DIR = MASTER_DB_DIR + "/" + "db_unlabeled_clips";
	public static final String MID_UNLABELED_DIR = MASTER_DB_DIR + "/" + "db_unlabeled_mid";
	public static final String CLASSIFIERS_DIR = MASTER_DB_DIR + "/" + "db_classifiers";
	public static final String CLIP_DB_DIR_EXT = "wav";
	public static final String GTP_DB_DIR_EXT = "gp4";
	public static final String MID_DB_DIR_EXT = "mid";
	public static final String TEMP_DB_DIR_EXT = "mid";
	public static final String J48_SERIAL_FILE = CLASSIFIERS_DIR + "/J48.obj";
	public static final String BAYES_SERIAL_FILE = CLASSIFIERS_DIR + "/BAYES.obj";
	public static final String CHROMA_SERIAL_FILE = CLASSIFIERS_DIR + "/Chroma.obj";
	public static final String ARFF_FILE = TEMP_DB_DIR + "/" + "AUDIO_CLASSIFICATION.arff";
	public static final String CODE_FILE = "code.m";
	public static final String CODE_FILE_COMMAND = "code";
	public static final String CHROMA_TRAIN_MATLAB_FILE = "train.m";
	public static final String CHROMA_CLASSIFY_MATLAB_FILE = "classify.m";
	
	//mine
	private DBManager clipDB;
	private DBManager gtpDB;
	private DBManager midDB;
	private DBManager tempDB;
	private DBManager uMidiDB;
	private DBManager uClipsDB;
	private GTPDBToMidDB gmConverter;
	private MidDBToClipDB mcConverter;
	private Classifier J48Classifier;
	private Classifier NaiveBayesClassifier;
	private DistanceBasedClassifier ChromaBasedClassifier;
	private DistanceBasedClassifier ChromaBasedClassifier2;
	private MelodyLearningTask mlt, mlt2;
	private int ctr=0;
			
	public UserOperations() {
		FFTCodeGenerator.setDbDir(CLIP_DB_DIR);
		clipDB = new DBManager(CLIP_DB_DIR, CLIP_DB_DIR_EXT);
		gtpDB = new DBManager(GTP_DB_DIR, GTP_DB_DIR_EXT);
		midDB = new DBManager(MID_DB_DIR, MID_DB_DIR_EXT);
		tempDB = new DBManager(TEMP_DB_DIR, TEMP_DB_DIR_EXT);
		uMidiDB = new DBManager(UserOperations.MID_UNLABELED_DIR, UserOperations.MID_DB_DIR_EXT);
		uClipsDB = new DBManager(UserOperations.CLIP_UNLABELED_DIR, UserOperations.CLIP_DB_DIR_EXT);

		gmConverter = new GTPDBToMidDB(gtpDB, midDB, tempDB);
		mcConverter = new MidDBToClipDB(midDB,clipDB);
		J48Classifier = WekaUtil.unserializeClassifier(J48_SERIAL_FILE);
		NaiveBayesClassifier = WekaUtil.unserializeClassifier(BAYES_SERIAL_FILE);
		ChromaBasedClassifier = (ScaleInvariantClassifier) WekaUtil.unserializeClassifier(CHROMA_SERIAL_FILE);
		mlt = new MelodyLearningTask("a1",20);
		mlt2 = new MelodyLearningTask("a1", 20);
		ChromaBasedClassifier2 = new DistanceBasedClassifier();
	}
	
	public void reset() {
		clipDB.clear();
		midDB.clear();
		uMidiDB.clear();
		uClipsDB.clear();
	}

	public void handleRecordTrainedTranscription() {
		
		//record
		String fileName = "test.wav";
		recordRoutine(fileName, CLIP_UNLABELED_DIR);
		File dir = new File(CLIP_UNLABELED_DIR);
		String[] inputFiles = {fileName};

		List<ChromaBasedTranscription> list = ChromaNoteTranscriber.transcribe(dir.getAbsolutePath()+"/", inputFiles, CHROMA_CLASSIFY_MATLAB_FILE);
		for(int x=0; x < inputFiles.length; x++) {
			ChromaBasedTranscription ct = list.get(x);
			Melody m = new Melody(ct.getPitchTranscription(), inputFiles[x]);
			Melody m2 = new Melody(ct.getNotes(), inputFiles[x]);
//			Melody belief = mlt.getBelief(m);
			JFugueUtil.play(m);
			System.out.println(m);
			System.out.println(m2 + "\n");
		}
		
	}

	public void handleTrainChromaRecord() {

		Map<String,List<Melody>> melodiesMap = TGMelodyExtractor.extractMelodies("db/db_gtp/starry_night.gp4",1,1,0);		//based on rests
		List<Melody> melodies = melodiesMap.get(new ArrayList(melodiesMap.keySet()).get(0));
		
		//record
/*		String fileName = "test.wav";
		recordRoutine(fileName, CLIP_UNLABELED_DIR);
		File dir = new File(CLIP_UNLABELED_DIR);
		String[] inputFiles = {fileName};

		//train
		System.out.println("TRAINING");
		List<ChromaBasedTranscription> list = ChromaNoteTranscriber.transcribe(dir.getAbsolutePath()+"/", inputFiles, CHROMA_TRAIN_MATLAB_FILE);
		ChromaBasedTranscription ct = list.get(0);
		System.out.println("ADDING " + inputFiles[0] + " " + ct.toString());
		Melody m = new Melody(ct.getNotes(), "MEL"+ctr);*/
		for(Melody m : melodies) {
			ChromaBasedClassifier2.addMelody(m);
		}
//		ctr++;
		
		//serialize
		WekaUtil.serializeClassifier(ChromaBasedClassifier, CHROMA_SERIAL_FILE);		
	}

	
	public void handleChromaClassificationRecord2() {
		
		//record
		String fileName = "test.wav";
		recordRoutine(fileName, CLIP_UNLABELED_DIR);
		File dir = new File(CLIP_UNLABELED_DIR);
		String[] inputFiles = {fileName};

		List<ChromaBasedTranscription> list = ChromaNoteTranscriber.transcribe(dir.getAbsolutePath()+"/", inputFiles, CHROMA_CLASSIFY_MATLAB_FILE);
		System.out.println("DONE TRANSCRIBING");
		for(int x=0; x < inputFiles.length; x++) {
			ChromaBasedTranscription ct = list.get(x);
			Melody m = new Melody(ct.getNotes(), inputFiles[x]);
			System.out.println("Label: " + (ChromaBasedClassifier2).classify(m).toUpperCase() + " " + ct.toString());;
		}
		
	}

	
	public void handleChromaClassificationRecord() {
		
		//record
		String fileName = "test.wav";
		recordRoutine(fileName, CLIP_UNLABELED_DIR);
		File dir = new File(CLIP_UNLABELED_DIR);
		String[] inputFiles = {fileName};

		List<ChromaBasedTranscription> list = ChromaNoteTranscriber.transcribe(dir.getAbsolutePath()+"/", inputFiles, CHROMA_CLASSIFY_MATLAB_FILE);
		System.out.println("DONE TRANSCRIBING");
		for(int x=0; x < inputFiles.length; x++) {
			ChromaBasedTranscription ct = list.get(x);
			Melody m = new Melody(ct.getNotes(), inputFiles[x]);
			System.out.println(inputFiles[x] + " " + (ChromaBasedClassifier).classify(m).toUpperCase() + " " + ct.toString());
		}
		
	}
	
	public void handleTrainChromaClassifier() {
		
		//train
		System.out.println("TRAINING");
		ChromaBasedClassifier = new ScaleInvariantClassifier();
		File dir = new File(clipDB.getDbDir());
		String[] inputFiles = {"a1--1.wav", "tl--1.wav"};
		List<ChromaBasedTranscription> list = ChromaNoteTranscriber.transcribe(dir.getAbsolutePath()+"/", inputFiles, CHROMA_TRAIN_MATLAB_FILE);
		for(int x=0; x < inputFiles.length; x++) {
			ChromaBasedTranscription ct = list.get(x);
			System.out.println("ADDING " + inputFiles[x] + " " + ct.toString());
			Melody m = new Melody(ct.getNotes(), inputFiles[x]);
			ChromaBasedClassifier.addMelody(m);
		}
		
		//serialize
		WekaUtil.serializeClassifier(ChromaBasedClassifier, CHROMA_SERIAL_FILE);		
	}
	
	public void handleGenerateChromaTrainingStats() {
				
		//run on db_clips
		File dir = new File(clipDB.getDbDir());
		System.out.println("TESTING");
		File[] files = clipDB.getDBFiles("tl");
		String[] inputFiles = new String[files.length];
		for(int x=0; x < files.length; x++) {
			inputFiles[x] = files[x].getName();
		}
		List<ChromaBasedTranscription> list = ChromaNoteTranscriber.transcribe(dir.getAbsolutePath()+"/", inputFiles, CHROMA_CLASSIFY_MATLAB_FILE);
		System.out.println("DONE TRANSCRIBING");
		for(int x=0; x < inputFiles.length; x++) {
			ChromaBasedTranscription ct = list.get(x);
			System.out.println(ct.toString_pt());
			Melody m = new Melody(ct.getPitchTranscription(), inputFiles[x]);
	//		Melody m2 = new Melody(ct.getNotes(), inputFiles[x]);
			Melody hypothesis = mlt.getBelief(m);
//			Melody hypothesis2 = mlt2.getBelief(m2);
			String jfStr = MelodyToJFugue.convert_pitch_melody(hypothesis);
			JFugueUtil.playString(jfStr);
			System.out.println(jfStr + " " + hypothesis + "\n");
//			JFugueUtil.playString(MelodyToJFugue.convert_basic(m));		
//			System.out.println(inputFiles[x] + " " + ((DistanceBasedClassifier)ChromaBasedClassifier).classify(m).toUpperCase() + " " + ct.toString());;
		}
		
		System.out.println("\nDONE.\n");
	}
		
	public void handleGenerateTrainingExamples() {
		//Enter file name in GTP to generate from
		String fileName = SysUtil.promptString("Type file name: ");		
		
		//get file and check validity
		if(!gtpDB.containsFile(fileName)) {
			System.out.println("Generatation Failed - Root File found found.\n");
		}
		else {			
			//generate examples using converter
			File f = gtpDB.getFile(fileName);
			gmConverter.convert(f);
			System.out.println("Training examples generated.\n");
		}		
	}
	
	public void handleExportToWavs() {
		//specific label or whole db?
		int choice = SysUtil.promptInt("(1) specific label or (2) whole db? :", 1,2);
		
		final int SPECIFIC_LABEL_CHOICE=1;
		final int WHOLE_DB_CHOICE=2;
				
		switch(choice) {
			case SPECIFIC_LABEL_CHOICE:
				
				//only export for a specific label
				String label = SysUtil.promptString("Enter label: ");
				if(!midDB.containsLabel(label)) {
					System.out.println("Export Failed -- Label not found.\n");
				}
				else {
					mcConverter.handleConversion(label);
					System.out.println("Conversion successful.\n");
				}
		
				break;
			
			case WHOLE_DB_CHOICE:
				
				//export whole db
				mcConverter.handleConversion();
				System.out.println("Conversion successful.\n");
				
				break;
		}
	}
	
	public void handleTrainClassifiers() {
		
		//generate matlab code for db
		FFTCodeGenerator.setDbDir(CLIP_DB_DIR);
		File[] files = (new File(CLIP_DB_DIR)).listFiles(new ExtFilter("wav"));
		SysUtil.writeFile(CODE_FILE, FFTCodeGenerator.getCodeTemplate(files));
		
		//run matlab script
		MatlabControlUtil.runFile(CODE_FILE_COMMAND);
		
		//Get DataSets from files
		List<String> series = clipDB.obtainSeries();
		String[] seriesArr = new String[series.size()];
		for(int x=0; x < seriesArr.length; x++) {
			seriesArr[x] = series.get(x);
		}
		List<FileDataSet> dataSets = getDataSets(seriesArr, files);

		//create feature table
		FeatureTable table = new FeatureTable();
		for(FileDataSet dSet : dataSets) {
			FeatureSet fs = dSet.computeFeatureSet();
			table.addFeatureSet(fs);			
		}		
		String tableStr = table.genWekaTable("AUDIO_CLASSIFICATION");
		
		//write arff file
		SysUtil.writeFile(ARFF_FILE,tableStr);
		
		//build classifiers and serialize
		Classifier c = WekaUtil.buildJ48Classifier(ARFF_FILE);
		WekaUtil.serializeClassifier(c, J48_SERIAL_FILE);
		Classifier c2 = WekaUtil.buildNaiveBayesClassifier(ARFF_FILE);
		WekaUtil.serializeClassifier(c2, BAYES_SERIAL_FILE);
		
		System.out.println("-----------J48 Output-----------");
		System.out.println(c);
		System.out.println("-----------J48 Output-----------");
		System.out.println("----------Bayes Output----------");
		System.out.println(c2);
		System.out.println("----------Bayes Output----------");		
		
		System.out.println("Classifiers built and trained.\n");
	}
	
	public static List<FileDataSet> getDataSets(String[] series, File[] files) {
		List<FileDataSet> rtn = new ArrayList<FileDataSet>();
		for(File file : files) {
			String seri = file.getName().substring(0, file.getName().lastIndexOf("--"));
			String fileName = file.getName().substring(0, file.getName().lastIndexOf("."));
			rtn.add(new FileDataSet(fileName, seri,series, CLIP_DB_DIR));
		}
		return rtn;
	}	
	
	public void handleDBClipRecord() {
		String label = SysUtil.promptString("Select a label: ");
		String fileName = clipDB.computeFileName(label);
		recordRoutine(fileName, CLIP_DB_DIR);		
	}
	
	public void handleClassificationRecord() {
	//	String fileName = SysUtil.promptString("Select a file name: ");
		String fileName = "test.wav";
		recordRoutine(fileName, CLIP_UNLABELED_DIR);
		
		//generate matlab code
/*		DBCodeGenerator.setDbDir(CLIP_UNLABELED_DIR);
		String code = DBCodeGenerator.getCodeTemplate_singleAudioFile(fileName);
		SysUtil.writeFile(CODE_FILE, code);
		
		//run matlab script
		MatLabControlInterface.runFile(CODE_FILE_COMMAND);
		
		//obtain possible series
		List<String> possibleSeries = clipDB.obtainSeries();
		String[] possibleSeriesNames = new String[possibleSeries.size()];
		for(int x=0; x < possibleSeries.size(); x++) {
			possibleSeriesNames[x] = possibleSeries.get(x);
		}
		
		//load file feature vector
		String fileNameMini = fileName.substring(0, fileName.lastIndexOf("."));
		FileDataSet fd = new FileDataSet(fileNameMini, possibleSeriesNames[0], possibleSeriesNames,CLIP_UNLABELED_DIR);
		FeatureSet fs = fd.computeFeatureSet();
		FeatureTable ft = new FeatureTable();
		ft.addFeatureSet(fs);
		
		//write to file
		String wekaFile = ft.genWekaTable("AUDIO_CLASSIFICATION");
		SysUtil.writeFile(ARFF_FILE, wekaFile);

		//plug into weka and get label
		System.out.println("-----------J48 Output-----------");
		WekaUtil.classify(ARFF_FILE, J48Classifier);
		System.out.println("-----------J48 Output-----------");
		System.out.println("----------Bayes Output----------");
		WekaUtil.classify(ARFF_FILE, NaiveBayesClassifier);
		System.out.println("----------Bayes Output----------\n");	*/	
	}
	
	public void recordRoutine(String fileName, String dir) {
//		System.out.println("Press enter to start recording.");
		try {
			Thread.sleep(2000);
//			BufferedReader r = new BufferedReader(new InputStreamReader(System.in));
//			r.readLine();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		System.out.println("RECORDING\n");
		SoundRecorderUtil.record(dir + "/" + fileName, 10);
		System.out.println("DONE RECORDING!\n");
	}			
}