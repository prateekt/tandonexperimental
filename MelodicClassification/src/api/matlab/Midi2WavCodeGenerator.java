package api.matlab;

public class Midi2WavCodeGenerator {
	
	public static String single_file_handler(String inputFile, String outputFile, double f_scale) {
		StringBuffer rtn = new StringBuffer();
		rtn.append("midi = readmidi('"+inputFile+"');\n");
		rtn.append("[y,Fs] = midi2audio(midi);\n");
		rtn.append("y = .95.*y./max(abs(y))\n");
		rtn.append("Fs = Fs * " + f_scale + ";");
		rtn.append("wavwrite(y,Fs, '"+outputFile+"');\n");
		return rtn.toString();
	}
}