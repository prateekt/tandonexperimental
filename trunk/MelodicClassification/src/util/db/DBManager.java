package util.db;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import util.file.ExtFilter;
import util.file.LabelFilter;
import util.file.MegaFilter;

public class DBManager {
	
	private String dbDir;
	private String ext;
	
	public DBManager(String dbDir, String ext) {
		this.dbDir = dbDir;
		this.ext = ext;
	}
	
	public void clear() {
		File[] files = getDBFiles_noFilter();
		for(File f : files) {
			f.delete();
		}
	}

	public File[] getDBFiles_noFilter() {
		File f = new File(dbDir);
		File[] rtn = f.listFiles();
		return rtn;
	}

	public File[] getDBFiles() {
		File f = new File(dbDir);
		File[] rtn = f.listFiles(new ExtFilter(ext));
		return rtn;
	}
	
	public File[] getDBFiles(String label) {
		if(!containsLabel(label)) {
			File[] files = new File[0];
			return files;
		}
		else {
			MegaFilter mf = new MegaFilter();
			mf.addFilter(new LabelFilter(label));
			mf.addFilter(new ExtFilter(ext));
			File dir = new File(dbDir);
			return dir.listFiles(mf);
		}
	}
	
	public boolean containsLabel(String label) {
		String f_name = computeFileName(label);
		return !(f_name.indexOf("--1.") > 1);
	}
	
	public boolean containsFile(String file) {
		File f = new File(dbDir + "/" + file);
		File[] files = getDBFiles();
		for(File filee: files) {
			if(f.getName().equalsIgnoreCase(filee.getName()))
				return true;
		}
		return false;
	}
	
	public File getFile(String file) {
		File[] files = getDBFiles();
		for(File filee: files) {
			if(file.equalsIgnoreCase(filee.getName()))
				return filee;
		}			
		return null;
	}
	
	public List<String> obtainSeries() {
		List<String> rtn = new ArrayList<String>();
		File dir = new File(dbDir);
		File[] files = dir.listFiles();
		for(File file : files) {
			if(file.isHidden())
				continue;
			String fileName = file.getName();
			String label = fileName.substring(0, fileName.lastIndexOf("--"));
			
			if(!rtn.contains(label))
				rtn.add(label);
		}
		
		return rtn;
	}

	public String computeFileName(String label) {
		File dir = new File(dbDir);
		File[] files = dir.listFiles(new LabelFilter(label));
		int maxIndex=0;
		for(File file : files) {
			try {
				String fileName = file.getName();
				int findex = Integer.parseInt(fileName.substring(fileName.indexOf(label)+label.length()+2, fileName.lastIndexOf(".")));
				if(findex > maxIndex)
					maxIndex = findex;
			}
			catch(Exception e) {}
		}
		
		return label + "--" + (maxIndex+1) + "." + ext;
	}
	
	public String computeFileName(String label, int index) {
		return label + "--" + index + "." + ext;
	}
	
	public int getNextIndex(String label) {
		String fileName = computeFileName(label);
		int findex = Integer.parseInt(fileName.substring(fileName.indexOf("--")+2, fileName.lastIndexOf(".")));
		return findex;
	}
	
	public String computeFilePath(String label, int index) {
		return dbDir + "/" + computeFileName(label,index);
	}
		
	public String computeFilePath(String label) {
		return dbDir + "/" + computeFileName(label);
	}
	
	public String getLabel(String file) {
		int hDex = file.lastIndexOf("--");
		int slashDex = file.lastIndexOf("/");
		int dotDex = file.lastIndexOf(".");
		
		if(hDex>-1 && slashDex>-1) {
			return file.substring(slashDex+1, hDex);
		}
		else if(slashDex>-1) {
			return file.substring(slashDex+1, dotDex);
		}
		else if(hDex>-1) {
			return file.substring(0, hDex);
		}
		
		return file.substring(0, dotDex);
	}

	/**
	 * @return the dbDir
	 */
	public String getDbDir() {
		return dbDir;
	}

	/**
	 * @return the ext
	 */
	public String getExt() {
		return ext;
	}
}
