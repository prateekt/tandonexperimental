package api.seqlib;

public class LCSAlignment {
	
	private String lcs;
	private char c;
	private int s1Index;
	private int s2Index;
		
	public LCSAlignment(String lcs, char c, int s1Index, int s2Index) {
		this.lcs = lcs;
		this.c = c;
		this.s1Index = s1Index;
		this.s2Index = s2Index;
	}
	
	public String toString() {
		return c + " " + s1Index + " " + s2Index;
	}

	/**
	 * @return the lcs
	 */
	public String getLcs() {
		return lcs;
	}

	/**
	 * @param lcs the lcs to set
	 */
	public void setLcs(String lcs) {
		this.lcs = lcs;
	}

	/**
	 * @return the c
	 */
	public char getC() {
		return c;
	}

	/**
	 * @param c the c to set
	 */
	public void setC(char c) {
		this.c = c;
	}

	/**
	 * @return the s1Index
	 */
	public int getS1Index() {
		return s1Index;
	}

	/**
	 * @param s1Index the s1Index to set
	 */
	public void setS1Index(int s1Index) {
		this.s1Index = s1Index;
	}

	/**
	 * @return the s2Index
	 */
	public int getS2Index() {
		return s2Index;
	}

	/**
	 * @param s2Index the s2Index to set
	 */
	public void setS2Index(int s2Index) {
		this.s2Index = s2Index;
	}
}
