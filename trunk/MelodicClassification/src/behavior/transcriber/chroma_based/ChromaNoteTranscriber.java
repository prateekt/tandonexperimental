package behavior.transcriber.chroma_based;
import api.matlab.*;
import behavior.transcriber.base.Note;

import java.util.*;



import util.sys.SysUtil;
public class ChromaNoteTranscriber {
	
	private static double MIN_INTENSITY = 0.4;
	
	public static void main(String[] args) {
		String inputDir = "/Users/prateek/Desktop/clip_db/";
		String[] inputFile = {"a2.wav"};
		List<ChromaBasedTranscription> list = ChromaNoteTranscriber.transcribe(inputDir, inputFile, "chroma.m");
		for(ChromaBasedTranscription t : list) {
			System.out.println(t.toString());
		}
	}

	public static List<ChromaBasedTranscription> transcribe(String inputDir, String[] inputFiles, String matlab_file2) {
		String matlab_file = matlab_file2;
		
		//get code
		String code = "";
		List<String> fileRoots = new ArrayList<String>();
		for(String inputFile : inputFiles) {
			String root = inputFile.substring(0, inputFile.indexOf("."));
			String arr_file_chroma_index = root + "_chroma_index.txt";
			String arr_file_chroma_max = root + "_chroma_intensity.txt";
			String arr_file_pitch_index = root + "_pitch_index.txt";
			String arr_file_pitch_max = root + "_pitch_intensity.txt";
			String arr_file_time_domain = root + "_time_domain.txt";

			fileRoots.add(root);
			code += ChromaCodeGenerator.single_file_handler(inputDir, inputFile, arr_file_pitch_index, arr_file_pitch_max, arr_file_chroma_index, arr_file_chroma_max, arr_file_time_domain);
		}
		
		//execute code in matlab
		MatlabControlUtil.runCode(code,matlab_file);
		
		//create rtn object
		List<ChromaBasedTranscription> rtn = new ArrayList<ChromaBasedTranscription>();		
		for(String root : fileRoots) {
			String arr_file_chroma_index = root + "_chroma_index.txt";
			String arr_file_chroma_max = root + "_chroma_intensity.txt";
			String arr_file_pitch_index = root + "_pitch_index.txt";
			String arr_file_pitch_max = root + "_pitch_intensity.txt";
			String arr_file_time_domain = root + "_time_domain.txt";

			//read index file in java as int array
			List<Double> arr_index = SysUtil.getArray(arr_file_chroma_index);
			List<Integer> list = SysUtil.dtoi(arr_index);
			List<Double> arr_max = SysUtil.getArray(arr_file_chroma_max);
			
			//also read pitch and time domain information
			List<Double> arr_index_pitch = SysUtil.getArray(arr_file_pitch_index);
			List<Integer> list_pitch = SysUtil.dtoi(arr_index_pitch);
			List<Double> arr_max_pitch = SysUtil.getArray(arr_file_pitch_max);
			List<Note> pitchTranscription = PitchTranscriber.getNotes(list_pitch, arr_max_pitch);
			List<Double> arr_time_domain = SysUtil.getArray(arr_file_time_domain);
			
			//convert java int array to notes
			List<Note> notes  = getNotes(list, arr_max);
		
			//apply filter
			applyIntensityFilter(notes, MIN_INTENSITY);
			applyDurationFilter(pitchTranscription,1);
			applyIntensityFilter(pitchTranscription, 0.1);
		
			//return
			ChromaBasedTranscription toAdd = new ChromaBasedTranscription(notes, pitchTranscription);
			rtn.add(toAdd);
		}
		
		return rtn;
	}
	
	public static void applyDurationFilter(List<Note> notes, int min_duration) {
		List<Note> toRemove = new ArrayList<Note>();
		for(Note n : notes) {
			if(n.getDuration() < min_duration) {
				toRemove.add(n);
			}
		}
		
		for(Note n : toRemove) {
			notes.remove(n);
		}	
	}
	
	public static void applyIntensityFilter(List<Note> notes, double min_intensity) {
		List<Note> toRemove = new ArrayList<Note>();
		
		for(Note n : notes) {
			if(n.getMaxIntensity() <= min_intensity) {
				toRemove.add(n);
//				n.setName("SILENCE");
			}
		}
		
		for(Note n : toRemove) {
			notes.remove(n);
		}
	}
		
	public static List<Note> getNotes(List<Integer> list, List<Double> intensityList) {
		
		List<Note> rtn = new ArrayList<Note>();
		
		int durCtr=0;
		int last=-1;
		List<Double> intensity = new ArrayList<Double>();

		for(int x=0; x < list.size(); x++) {
			int current = list.get(x);
			double currentIntensity = intensityList.get(x);
			
			//store if note period ended
			if(current!=last && last!=-1) {
				String noteName = getNoteName(last);
				Note n = new Note(noteName, durCtr, intensity);
				rtn.add(n);
				durCtr=0;
			}
			
			if(x==list.size()-1 && durCtr!=0) {
				String noteName = getNoteName(current);
				intensity = new ArrayList<Double>();
				intensity.add(currentIntensity);
				Note n = new Note(noteName,durCtr+1,intensity);
				rtn.add(n);
			}
			
			//always
			intensity.add(currentIntensity);
			durCtr++;
			last=current;
		}
		
		return rtn;
	}
	
	public static String getNoteName(int note) {
		String[] notes = {"C", "C#","D","D#","E","F","F#","G","G#","A","A#","B"};
		return notes[note-1];
	}	
}
