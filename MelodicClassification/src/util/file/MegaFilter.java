package util.file;
import java.io.*;
import java.util.*;

public class MegaFilter implements FileFilter {
	
	private List<FileFilter> filters;
	
	public MegaFilter() {
		filters = new ArrayList<FileFilter>();
	}
	
	public void addFilter(FileFilter f) {
		filters.add(f);
	}
		
	public boolean accept(File pathname) {
		for(FileFilter f : filters) {
			if(!f.accept(pathname))
				return false;
		}
		return true;
	}		
}
