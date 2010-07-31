package util.math.MelodySegmentation;
import java.util.*;
public class PossibleTagging implements Comparable<PossibleTagging> {
	
	private NGram original;
	private List<NGram> followingElements;
	private int n;
	
	public PossibleTagging(NGram original, List<NGram> followingElements, int n) {
		this.original = original;
		this.followingElements = followingElements;
		this.n = n;
	}
	
	@Override
	public String toString() {
		StringBuffer rtn = new StringBuffer();
		rtn.append(original.toString() + " ");
		for(NGram n : followingElements) {
			rtn.append(n.toString() + " ");
		}
		
		return rtn.toString().trim();
	}
	
	public int getSize() {
		return followingElements.size();
	}

	/**
	 * @return the original
	 */
	public NGram getOriginal() {
		return original;
	}

	/**
	 * @param original the original to set
	 */
	public void setOriginal(NGram original) {
		this.original = original;
	}

	/**
	 * @return the followingElements
	 */
	public List<NGram> getFollowingElements() {
		return followingElements;
	}

	/**
	 * @param followingElements the followingElements to set
	 */
	public void setFollowingElements(List<NGram> followingElements) {
		this.followingElements = followingElements;
	}
	
	/**
	 * @return the n
	 */
	public int getN() {
		return n;
	}

	/**
	 * @param n the n to set
	 */
	public void setN(int n) {
		this.n = n;
	}

	public int compareTo(PossibleTagging o) {
		return (o.getSize() - getSize());
	}	
}
