package api.weka;
import weka.core.converters.ConverterUtils.DataSource;
import weka.core.Instances;
import weka.core.Instance;
import weka.classifiers.Classifier;
import weka.classifiers.trees.J48;
import weka.core.converters.ArffLoader;
import java.io.*;
import weka.classifiers.bayes.NaiveBayesUpdateable;


public class WekaUtil {
	
	public static J48 buildJ48Classifier(String dataFile) {		
		try {

			//load data
			ArffLoader loader = new ArffLoader();
			loader.setFile(new File(dataFile));
			Instances structure = loader.getStructure();
			structure.setClassIndex(structure.numAttributes() - 1);
			
			//build classifier
			String[] options = new String[1];
			options[0] = "-U";
			J48 tree = new J48();
			tree.setOptions(options);
			tree.buildClassifier(structure);
			
			return tree;
			
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static NaiveBayesUpdateable buildNaiveBayesClassifier(String dataFile) {
		try {

			//load data
			ArffLoader loader = new ArffLoader();
			loader.setFile(new File(dataFile));
			Instances structure = loader.getStructure();
			structure.setClassIndex(structure.numAttributes() - 1);
			
			// train NaiveBayes
			NaiveBayesUpdateable nb = new NaiveBayesUpdateable();
			nb.buildClassifier(structure);
			Instance current;
			while ((current = loader.getNextInstance(structure)) != null)
			nb.updateClassifier(current);			
			
			return nb;
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}		
	}
	
	public static void serializeClassifier(Classifier c, String file) {		
		try {
			
			//Prepare classifier to be serialized
			ClassifierWrapper cw = new ClassifierWrapper(c);
			
			// Write to disk with FileOutputStream
			FileOutputStream f_out = new FileOutputStream(file);

			// Write object with ObjectOutputStream
			ObjectOutputStream obj_out = new ObjectOutputStream (f_out);

			// Write object out to disk
			obj_out.writeObject (cw);

			//close streams
			obj_out.close();
			f_out.close();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static Classifier unserializeClassifier(String file) {
		Classifier rtn = null;
		try {
			// Read from disk using FileInputStream
			FileInputStream f_in = new FileInputStream(file);

			// Read object using ObjectInputStream
			ObjectInputStream obj_in = new ObjectInputStream (f_in);

			// Read an object
			Object obj = obj_in.readObject();

			if (obj instanceof ClassifierWrapper) {
				ClassifierWrapper cw = (ClassifierWrapper) obj;
				rtn = cw.getC();
			}
		}
		catch(Exception e) {}
		return rtn;
	}
	
	public static void classify(String dataFile, Classifier tree) {
		
		try {
			// load unlabeled data and set class attribute
			Instances unlabeled = DataSource.read(dataFile);
			unlabeled.setClassIndex(unlabeled.numAttributes() - 1);
	
			// create copy
			Instances labeled = new Instances(unlabeled);
			
			// label instances
			for (int i = 0; i < unlabeled.numInstances(); i++) {
			double clsLabel = tree.classifyInstance(unlabeled.instance(i));
				labeled.instance(i).setClassValue(clsLabel);
			}
			
			System.out.println("LABEL: " + getLabelFromTrace(labeled.toString()));
		}
		catch (Exception e) {
			e.printStackTrace();
		}		
	}
	
	private static String getLabelFromTrace(String trace) {
		int comma = trace.lastIndexOf(",");
		return trace.substring(comma+1, trace.length());
	}
}
