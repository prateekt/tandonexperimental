package behavior.follower.MCS;
import java.util.*;

public class AcceptablePath {
	
	private List<MelodyOccurence> path;
	
	public AcceptablePath(List<MelodyOccurence> path) {
		this.path = path;
	}
	
	public int getNumMelodies() {
		return path.size();
	}
	
	public int getSpanUnits() {
		int sum=0;
		for(MelodyOccurence m : path) {
			int length = m.getLength();
			sum+=length;
		}
		return sum;
	}
	
	public String toString() {
		StringBuffer rtn = new StringBuffer();
		rtn.append("{");
		for(MelodyOccurence o : path) {
			rtn.append(o.toString() + " ");
		}
		return rtn.toString().trim() + "}";
	}

	/**
	 * @return the path
	 */
	public List<MelodyOccurence> getPath() {
		return path;
	}

	/**
	 * @param path the path to set
	 */
	public void setPath(List<MelodyOccurence> path) {
		this.path = path;
	}
}
