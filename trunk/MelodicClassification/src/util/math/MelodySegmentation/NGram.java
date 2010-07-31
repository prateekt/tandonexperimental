package util.math.MelodySegmentation;
import java.util.*;

public class NGram {
	
	private List<String> grams;
	private int spanMin;
	private int spanMax;
	
	public NGram(List<String> grams, int spanMin, int spanMax) {
		this.grams = grams;
		this.spanMin = spanMin;
		this.spanMax = spanMax;
	}
	
	public int size() {
		return grams.size();
	}
	
	@Override
	public String toString() {
		StringBuffer rtn = new StringBuffer();
		rtn.append("\"");
		for(String gram : grams) {
			rtn.append(gram);
		}
		rtn.append("\"");
		return rtn.toString();
	}

	public String compareStr() {
		StringBuffer rtn = new StringBuffer();
		for(String gram : grams) {
			rtn.append(gram);
		}
		return rtn.toString();
	}

	/**
	 * @return the grams
	 */
	public List<String> getGrams() {
		return grams;
	}

	/**
	 * @param grams the grams to set
	 */
	public void setGrams(List<String> grams) {
		this.grams = grams;
	}

	/**
	 * @return the spanMin
	 */
	public int getSpanMin() {
		return spanMin;
	}

	/**
	 * @param spanMin the spanMin to set
	 */
	public void setSpanMin(int spanMin) {
		this.spanMin = spanMin;
	}

	/**
	 * @return the spanMax
	 */
	public int getSpanMax() {
		return spanMax;
	}

	/**
	 * @param spanMax the spanMax to set
	 */
	public void setSpanMax(int spanMax) {
		this.spanMax = spanMax;
	}
}
