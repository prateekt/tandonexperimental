package util.file;

import java.io.File;
import java.io.FileFilter;

public class ExtFilter implements FileFilter {
	
	private String ext;
	
	public ExtFilter(String ext) {
		this.ext = ext;
	}
	
	public boolean accept(File pathname) {
		if(pathname.getName().endsWith("."+ext)) {
			return true;
		}
		else {
			return false;
		}
	}	
}