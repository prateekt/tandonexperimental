package api.weka;
import java.util.*;

public class FeatureSet {
	
	private List<Feature> impl;
	private Feature label;
	
	public FeatureSet() {
		impl = new ArrayList<Feature>();
	}
	
	public void addFeature(Feature f) {
		impl.add(f);
	}
	
	public void removeFeature(Feature f) {
		impl.remove(f);
	}
	
	public Feature get(int index) {
		return impl.get(index);
	}
	
	public int getSize() {
		return impl.size();
	}

	@Override
	public String toString() {
		StringBuffer rtn = new StringBuffer();
		for(Feature f : impl) {
			rtn.append(f + ",");
		}
		rtn.append(label.getValue());
		return rtn.toString();
	}
	
	/**
	 * @return the impl
	 */
	public List<Feature> getImpl() {
		return impl;
	}

	/**
	 * @param impl the impl to set
	 */
	public void setImpl(List<Feature> impl) {
		this.impl = impl;
	}

	/**
	 * @return the label
	 */
	public Feature getLabel() {
		return label;
	}

	/**
	 * @param label the label to set
	 */
	public void setLabel(Feature label) {
		this.label = label;
	}
}
