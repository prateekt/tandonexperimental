package api.weka;

import java.io.Serializable;

import weka.classifiers.Classifier;

public class ClassifierWrapper implements Serializable {
	
	private Classifier c;
	
	public ClassifierWrapper(Classifier c) {
		this.c =c;
	}

	/**
	 * @return the c
	 */
	public Classifier getC() {
		return c;
	}

	/**
	 * @param c the c to set
	 */
	public void setC(Classifier c) {
		this.c = c;
	}
	
}
