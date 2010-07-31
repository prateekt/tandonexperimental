package api.tuxguitar.MelodyStructureMiner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import behavior.transcriber.base.Melody;

import util.math.MelodySegmentation.NGram;
import util.math.MelodySegmentation.NGramMath;

public class StructureMining {
	
	/*public static void main(String[] args) {
//		List<Melody> melodies = extractMelodies("db/db_gtp/jm.gp4",0,0);		//based on rests
		List<Melody> melodies = TGMelodyExtractor.extractMelodies("db/db_gtp/starry_night.gp4",1,1,0);		//based on rests
		Map<String,Integer> melCount = getMelodyCount(melodies); //count unique melodies
		Map<String, String> sMap = getStructuralMapping(melCount); //map unique to char
		String ms = getMelodicStructure(melodies,sMap); //melodies to char_str
		System.out.println(ms);
		
		Map<String, Integer> words = SpanningSetMath.getTopWords(ms);
		Map<String,Map<String,Integer>> iSets = SpanningSetMath.getIndepedentSets(ms,words);
		for(String word : iSets.keySet()) {
			Map<String,Integer> set = iSets.get(word);
			System.out.print(word + ": ");
			System.out.println(SpanningSetMath.getTableStr(set));
		}
		
		List<List<String>> sp = SpanningSetMath.getSpanningSets(ms);
		for(List<String> s : sp) {
			String set = SpanningSetMath.getSpanSetStr(s);
			System.out.println(set);
		}
		System.out.println("FINAL: " + sp.size());
	}*/

	public static Map<String, Integer> getTable(String str) {
		String[] toks = str.split(" ");
		Map<String,Integer> rtn = new HashMap<String,Integer>();
		for(String tok : toks) {
			if(!rtn.containsKey(tok)) {
				rtn.put(tok, 1);
			}
			else {
				rtn.put(tok, rtn.get(tok)+1);
			}
		}
		return rtn;
	}
	
	public static List<String> getUniqueTokens(String s) {
		List<String> rtn = new ArrayList<String>();
		for(int x=0; x < s.length(); x++) {
			char c = s.charAt(x);
			if(!rtn.contains(""+c))
				rtn.add(""+c);
		}
		return rtn;
	}

	public static int getIndex(List<String> dict, String s) {
		int index=-1;
		for(int y=0; y < dict.size(); y++) {
			String str = dict.get(y);
			if(str.equalsIgnoreCase(s))
				index = y;
		}
		return index;
	}
	
	public static String reconstruct(String indexStr, List<String> dict) {
		String[] toks = indexStr.split(" ");
		String rtn = "";
		for(String tok : toks) {
			rtn+= dict.get(Integer.parseInt(tok));
		}
		return rtn;
	}
	
	public static String useLWZCompression(String content, List<String> dict) {
		String rtn="";
		String s="";
		char ch='\0';
		for(int x=0; x < content.length(); x++) {
			ch = content.charAt(x);
			if(dict.contains(s+ch)) {
				s = s+ch;
			}
			else {
				//encode s to output file
				rtn+= " "+getIndex(dict,s);
				
				//add s+ch to dict
				dict.add(s+ch);
				s= ""+ch;				
			}
	    }
		rtn+= " " + getIndex(dict,s);
		
		return rtn.trim();
	}
	
	public static Map<String, String> extractHierarchialMelodicStructure(String melStructure, Map<String, String> sMap) {
		Map<String,String> rtn = new LinkedHashMap<String,String>();
		int ctr=0;
		String currentStructure = melStructure;
		while(true) {
			Map<Integer,List<NGram>> nGramMap  = NGramMath.generateGramsInRange(currentStructure, 1, currentStructure.length());
			Map<String,Integer> countTable = getNGramCountTable(nGramMap);
			boolean found = false;
			for(String key : countTable.keySet()) {
				int count = countTable.get(key);
				if(count > 1) {
					rtn.put(key, ""+ctr);
					key = key.substring(1, key.length()-1);
					System.out.println(currentStructure + " " + key);
					currentStructure = currentStructure.replaceAll(key, ""+ctr);
					System.out.println(" " +currentStructure);
//					System.out.println(key+" " +currentStructure + " " + ctr);
					ctr++;
					found = true;
				}
			}
			
			if(!found)
				break;
		}
	//	System.out.println(currentStructure);
		return rtn;
	}
	
	public static Map<String, Integer> getNGramCountTable(Map<Integer, List<NGram>> ngrams) {
		Map<String,Integer> rtn = new TreeMap<String,Integer>();
		for(int i : ngrams.keySet()) {
			List<NGram> grams = ngrams.get(i);
			for(NGram g : grams) {
				if(!rtn.containsKey(g.toString())) {
					rtn.put(g.toString(), 1);
				}
				else {
					rtn.put(g.toString(), rtn.get(g.toString())+1);
				}
			}
		}
		return rtn;
	}
	
	public static String getMelodicStructure(List<Melody> melodies, Map<String,String> structureMap) {
		String rtn="";
		for(Melody m : melodies) {
			String struct = structureMap.get(m.compareStr());
			rtn+=struct;
		}
		return rtn;
	}
	
	public static Map<String, String> getStructuralMapping(Map<String,Integer> uniqueMelodies) {
		Map<String, String> rtn = new LinkedHashMap<String,String>();
		char c = 'A';
		for(String melStr : uniqueMelodies.keySet()) {
			rtn.put(melStr, ""+c);
			c++;
			if(c=='{')
				c=(char) (c+5);
			if(c=='[')
				c=(char) (c+5);
		}
		return rtn;
	}
	
	public static Map<String,Integer> getMelodyCount(List<Melody> melodies) {
		
		Map<String, Integer> rtn = new LinkedHashMap<String,Integer>();
		for(Melody m : melodies) {
			if(!rtn.containsKey(m.compareStr())) {
				rtn.put(m.compareStr(), 1);
			}
			else {
				rtn.put(m.compareStr(), rtn.get(m.compareStr())+1);
			}
		}	
		
		return rtn;
	}	
}
