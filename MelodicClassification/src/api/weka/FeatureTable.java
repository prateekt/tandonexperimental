package api.weka;
import java.util.*;

public class FeatureTable {
	
	private List<FeatureSet> impl;
	
	public FeatureTable() {
		impl = new ArrayList<FeatureSet>();
	}
	
	public void addFeatureSet(FeatureSet s) {
		impl.add(s);
	}
	
	public int getSize() {
		return impl.size();
	}
		
	public String genWekaTable(String title) {
		StringBuffer rtn = new StringBuffer();
		
		//generate rel section
		rtn.append("@relation " + title+"\n\n");
		
		//generate attributes section
		FeatureSet sampleFeatureSet = impl.get(0);		
		for(int x=0; x < sampleFeatureSet.getSize(); x++) {
			Feature sampleFeature = sampleFeatureSet.get(x);
			rtn.append(sampleFeature.genWekaAttrString() + "\n");
		}
		rtn.append(sampleFeatureSet.getLabel().genWekaAttrString() + "\n");
		rtn.append("\n");
		
		//generate data section
		rtn.append("@data\n");
		for(FeatureSet fs : impl) {
			rtn.append(fs + "\n");
		}
		
		return rtn.toString();
	}
}
