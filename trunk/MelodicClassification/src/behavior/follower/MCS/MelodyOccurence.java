package behavior.follower.MCS;

import api.seqlib.LCSAlignment;
import behavior.transcriber.base.Melody;
import java.util.*;

public class MelodyOccurence {
	
	private Melody m;
	private List<LCSAlignment> alignment;
	
	public MelodyOccurence(Melody m, List<LCSAlignment> alignment) {
		this.m = m;
		this.alignment = alignment;
	}
	
	public int getLength() {
		return getEnd() - getStart();
	}
	
	public String toString() {
		return m.getName() + "[" + getStart() + ":" + getEnd() + "]"; 
	}
	
	public int getStart() {
		return alignment.get(0).getS1Index();
	}
	
	public int getEnd() {
		return alignment.get(alignment.size()-1).getS1Index();
	}

	/**
	 * @return the m
	 */
	public Melody getM() {
		return m;
	}

	/**
	 * @param m the m to set
	 */
	public void setM(Melody m) {
		this.m = m;
	}

	/**
	 * @return the alignment
	 */
	public List<LCSAlignment> getAlignment() {
		return alignment;
	}

	/**
	 * @param alignment the alignment to set
	 */
	public void setAlignment(List<LCSAlignment> alignment) {
		this.alignment = alignment;
	}	
}
