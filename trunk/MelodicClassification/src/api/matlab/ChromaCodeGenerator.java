package api.matlab;
import util.sys.*;

public class ChromaCodeGenerator {
	
	public static String single_file_handler(String inputDir, String inputFile, String outputFile_PitchIndex, String outputFile_PitchIntensity, String outputFile_ChromaIndex, String outputFile_ChromaIntensity, String outputFile_TimeDomain) {
		StringBuffer2 rtn = new StringBuffer2();
		
		rtn.appendN("parameter.message = 0;");
		rtn.appendN("[f_audio,sideinfo] = wav_to_audio('"+inputDir+"','','"+inputFile+"',parameter);");
		rtn.appendN("parameter.win_len = 4410;");
		rtn.appendN("parameter.fs = sideinfo.wav.fs;");
		rtn.appendN("parameter.save = 0;");
		rtn.appendN("parameter.visualize = 0;");
		rtn.appendN("[f_pitch,sideinfo] = audio_to_pitchSTMSP_via_FB(f_audio,parameter,sideinfo);");
		rtn.appendN("parameter.featureRate = sideinfo.pitchSTMSP.featureRate;");

		//compute pitch representation
		rtn.appendN("log_f_pitch = log(5*f_pitch+1)");

		rtn.appendN("MAX_INTENSITY_PITCH = [];");
		rtn.appendN("MAX_INDEX_PITCH = [];");
		rtn.appendN("for i=1:size(log_f_pitch,1)");
		rtn.appendT("MAX_INTENSITY_PITCH(i) = log_f_pitch(1,1);",1);
		rtn.appendT("MAX_INDEX_PITCH(i) = 1;",1);
		rtn.appendN("end");
		rtn.appendN("for i=1:size(log_f_pitch,1)");
		rtn.appendT("for j=1:120",1);
		rtn.appendT("if log_f_pitch(i,j) > MAX_INTENSITY_PITCH(i)",2);
		rtn.appendT("MAX_INTENSITY_PITCH(i) = log_f_pitch(i,j);",3);
		rtn.appendT("MAX_INDEX_PITCH(i) = j;",3);
		rtn.appendT("end",3);
		rtn.appendT("end",2);
		rtn.appendT("end",1);
		
		//compute chroma representation
		rtn.appendN("[f_chroma_norm,f_chroma,sideinfo] = pitchSTMSP_to_chroma(f_pitch,parameter,sideinfo);");
		rtn.appendN("seg_num = size(f_chroma_norm, 1);");
		rtn.appendN("t=(0:seg_num-1)/parameter.featureRate;");
		rtn.appendN("MAX_INTENSITY_CHROMA = [];");
		rtn.appendN("MAX_INDEX_CHROMA = [];");
		rtn.appendN("for i=1:seg_num");
		rtn.appendT("MAX_INTENSITY_CHROMA(i) = f_chroma_norm(1,1);",1);
		rtn.appendT("MAX_INDEX_CHROMA(i) = 1;",1);
		rtn.appendN("end");
		rtn.appendN("for i=1:seg_num");
		rtn.appendT("for j=1:12",1);
		rtn.appendT("if f_chroma_norm(i,j) > MAX_INTENSITY_CHROMA(i)",2);
		rtn.appendT("MAX_INTENSITY_CHROMA(i) = f_chroma_norm(i,j);",3);
		rtn.appendT("MAX_INDEX_CHROMA(i) = j;",3);
		rtn.appendT("end",3);
		rtn.appendT("end",2);
		rtn.appendT("end",1);

		//save
		rtn.appendN("fid = fopen('"+outputFile_ChromaIndex+"','wt');");
		rtn.appendN("fprintf(fid,'%i ',MAX_INDEX_CHROMA);");
		rtn.appendN("fid = fopen('"+outputFile_ChromaIntensity+"','wt');");
		rtn.appendN("fprintf(fid,'%i ',MAX_INTENSITY_CHROMA);");
		rtn.appendN("fid = fopen('"+outputFile_PitchIndex+"','wt');");
		rtn.appendN("fprintf(fid,'%i ',MAX_INDEX_PITCH);");
		rtn.appendN("fid = fopen('"+outputFile_PitchIntensity+"','wt');");
		rtn.appendN("fprintf(fid,'%i ',MAX_INTENSITY_PITCH);");
		rtn.appendN("fid = fopen('"+outputFile_TimeDomain+"','wt');");
		rtn.appendN("fprintf(fid,'%i ',f_audio);");
		
		return rtn.toString();
	}
}
