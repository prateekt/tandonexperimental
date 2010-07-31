package api.matlab;
import java.io.*;
import util.sys.SysUtil;

public class FFTCodeGenerator {
		
	private static String dbDir;
	
	public static void main(String[] args) {
		String[] audioFiles = {"a1.wav", "a2.wav", "a3.wav", "a4.wav", "a5.wav", "b1.wav", "b2.wav", "b3.wav", "b4.wav", "b5.wav"};
		SysUtil.writeFile("code.m", getCodeTemplate(audioFiles));
	}
	
	public static String getCodeTemplate(File[] files) {
		String[] fileNames = new String[files.length];
		for(int x=0; x < files.length; x++) {
			fileNames[x] = files[x].getName();
		}		
		return getCodeTemplate(fileNames);
	}
	
	public static String getCodeTemplate(String[] audioFiles) {
		StringBuffer rtn = new StringBuffer();
		for(String audioFile : audioFiles) {
			rtn.append(getCodeTemplate_singleAudioFile(audioFile));
		}
		return rtn.toString();
	}
	
	public static String getCodeTemplate_singleAudioFile(String audioFile) {
		StringBuffer rtn = new StringBuffer();
		rtn.append("[wave,fs]=wavread('" +dbDir + "/" + audioFile+"');\n");
		rtn.append("t=0:1/fs:(length(wave)-1)/fs;\n");
		rtn.append("n=length(wave)-1;\n");
		rtn.append("f=0:fs/n:fs;\n");
		rtn.append("wavefft=abs(fft(wave));\n");
		rtn.append(writeFile_matlab_template(dbDir + "/" + audioFile,"wave"));
		rtn.append(writeFile_matlab_template(dbDir + "/" + audioFile,"t"));
		rtn.append(writeFile_matlab_template(dbDir + "/" + audioFile,"f"));
		rtn.append(writeFile_matlab_template(dbDir + "/" + audioFile,"wavefft"));
		return rtn.toString();
	}
	
	public static String writeFile_matlab_template(String audioFile, String variable) {
		String fname = audioFile.substring(0,(audioFile.indexOf("."))) + "_" + variable +".txt";
		StringBuffer rtn = new StringBuffer();
		rtn.append("fid = fopen('"+fname+"','wt');\n");
		rtn.append("fprintf(fid,'%f ',"+variable+");\n");
		return rtn.toString();
	}

	/**
	 * @return the dbDir
	 */
	public static String getDbDir() {
		return dbDir;
	}

	/**
	 * @param dbDir the dbDir to set
	 */
	public static void setDbDir(String dbDir) {
		FFTCodeGenerator.dbDir = dbDir;
	}
}

