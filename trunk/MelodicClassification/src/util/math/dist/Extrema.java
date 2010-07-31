package util.math.dist;
public class Extrema implements Comparable<Extrema >{
	private int index;
	private double value;
	private boolean maxima;
	
	public Extrema(boolean maxima, double value, int index) {
		this.maxima = maxima;
		this.value = value;
		this.index = index;
	}
	
	public Extrema(boolean maxima) {
		this.maxima = maxima;	
		index = -1;
		if(maxima)
			value = Double.MIN_VALUE;
		else
			value = Double.MAX_VALUE;
	}


	/**
	 * @return the index
	 */
	public int getIndex() {
		return index;
	}


	/**
	 * @param index the index to set
	 */
	public void setIndex(int index) {
		this.index = index;
	}


	/**
	 * @return the value
	 */
	public double getValue() {
		return value;
	}


	/**
	 * @param value the value to set
	 */
	public void setValue(double value) {
		this.value = value;
	}


	/**
	 * @return the maxima
	 */
	public boolean isMaxima() {
		return maxima;
	}


	/**
	 * @param maxima the maxima to set
	 */
	public void setMaxima(boolean maxima) {
		this.maxima = maxima;
	}
	
	@Override
	public String toString() {
		return "[" + index + "::" + value + "]";
	}

	public int compareTo(Extrema o) {
		return (o.getIndex() - getIndex());
	}
	
}