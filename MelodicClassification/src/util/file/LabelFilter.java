package util.file;

import java.io.File;
import java.io.FileFilter;

public class LabelFilter implements FileFilter {

	private String label;
	
	public LabelFilter(String label) {
		this.label = label;
	}
	
	public boolean accept(File pathname) {
		if(pathname.getName().toLowerCase().indexOf(label.toLowerCase()) > -1) {
			return true;
		}
		return false;
	}	
}
