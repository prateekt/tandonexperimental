package util.file;
import java.util.ArrayList;
import java.util.List;

import api.weka.*;

import util.math.dist.Extrema;
import util.math.dist.ExtremaMath;
import util.sys.SysUtil;

public class FileDataSet {
	
	private List<Double> t;
	private List<Double> wave;
	private List<Double> f;
	private List<Double> wavefft;
	private String seriesName;
	private String label;
	private List<String> possibleLabels;

	public FileDataSet(String fileName, String seriesName, String[] possibleSeriesNames, String dbDir) {
		this.seriesName = seriesName;
		t = SysUtil.getArray(dbDir + "/" +fileName + "_t.txt");
		wave = SysUtil.getArray(dbDir + "/" +fileName + "_wave.txt");
		f = SysUtil.getArray(dbDir +"/" + fileName + "_f.txt");
		wavefft = SysUtil.getArray(dbDir +"/" + fileName + "_wavefft.txt");
		
		//compute globals
		this.label = seriesName;
		possibleLabels = new ArrayList<String>();
		for(String psn : possibleSeriesNames) {
			if(!possibleLabels.contains(psn)) {
				possibleLabels.add(psn);
			}
		}
	}
	
/*	public FileDataSet(String seriesName, String[] possibleSeriesNames) {
		this.seriesName = seriesName;
		t = SysUtil.getArray(ProofOfConcept.MAIN_PATH + seriesName + "_t.txt");
		wave = SysUtil.getArray(ProofOfConcept.MAIN_PATH + seriesName + "_wave.txt");
		f = SysUtil.getArray(ProofOfConcept.MAIN_PATH + seriesName + "_f.txt");
		wavefft = SysUtil.getArray(ProofOfConcept.MAIN_PATH + seriesName + "_wavefft.txt");
		
		//compute globals
		label = seriesName.substring(0,1);
		possibleLabels = new ArrayList<String>();
		for(String psn : possibleSeriesNames) {
			String possibleLabel = psn.substring(0,1);
			if(!possibleLabels.contains(possibleLabel)) {
				possibleLabels.add(possibleLabel);
			}
		}
	}*/
	
	public FeatureSet computeFeatureSet() {
		FeatureSet rtn = new FeatureSet();
		int featureCtr=0;
		
		/**Feature Objects**/
				
		//1. Top 3 local maxima in freq domain
		Extrema[] maximas = ExtremaMath.computeNMaxima(wavefft, 20);
		for(Extrema maxima : maximas) {
			double featureVal = f.get(maxima.getIndex()/2);
			Feature<Double> f = new Feature<Double>("d" + featureCtr, Type.REAL, featureVal);
			rtn.addFeature(f);
			featureCtr++;
		}
		
		//2. Top 3 local minima in freq domain
		Extrema[] minimas = ExtremaMath.computeNMinima(wavefft, 20);
		for(Extrema minima : minimas) {
			double featureVal = f.get(minima.getIndex()/2);
			Feature<Double> f = new Feature<Double>("d" + featureCtr, Type.REAL, featureVal);
			rtn.addFeature(f);
			featureCtr++;
		}
		
		/**LABEL**/
		Feature<String> labelObj = new Feature<String>("MELODY", Type.DISCRETE, label);
		labelObj.setValue(label);
		labelObj.setOptions(possibleLabels);
		rtn.setLabel(labelObj);
		
		
		return rtn;
	}
	
	/**
	 * @return the t
	 */
	public List<Double> getT() {
		return t;
	}

	/**
	 * @param t the t to set
	 */
	public void setT(List<Double> t) {
		this.t = t;
	}

	/**
	 * @return the wave
	 */
	public List<Double> getWave() {
		return wave;
	}

	/**
	 * @param wave the wave to set
	 */
	public void setWave(List<Double> wave) {
		this.wave = wave;
	}

	/**
	 * @return the f
	 */
	public List<Double> getF() {
		return f;
	}

	/**
	 * @param f the f to set
	 */
	public void setF(List<Double> f) {
		this.f = f;
	}

	/**
	 * @return the wavefft
	 */
	public List<Double> getWavefft() {
		return wavefft;
	}

	/**
	 * @param wavefft the wavefft to set
	 */
	public void setWavefft(List<Double> wavefft) {
		this.wavefft = wavefft;
	}	
}
