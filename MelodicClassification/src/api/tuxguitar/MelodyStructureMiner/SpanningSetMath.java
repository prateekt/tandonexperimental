package api.tuxguitar.MelodyStructureMiner;

import java.util.*;

import util.math.MelodySegmentation.NGram;
import util.math.MelodySegmentation.NGramMath;

public class SpanningSetMath {
	
	public static void main(String[] args) {
		getTopWords("AAJBAAJ");
	}
		
	public static Map<String,Map<String,Integer>> getIndepedentSets(String corpus, Map<String,Integer> words2) {
		Map<String, Map<String,Integer>> rtn = new LinkedHashMap<String, Map<String,Integer>>();
		
		List<String> words = new ArrayList<String>(words2.keySet());
		
		for(String word : words) {
			Map<String,Integer> iSet = new HashMap<String,Integer>();
			
			for(String word2 : words) {
				if(word==word2)
					continue;
				
				//replace test
				String word_r_str = corpus.replaceAll(word, "1");
				String word2_r_str = corpus.replaceAll(word2, "1");
				
				if(word.contains(word2) || word2.contains(word)) {}
				else if(word_r_str.indexOf(word2)==-1 || word2_r_str.indexOf(word)==-1){}
				else {
					String r_str_ctr = word_r_str.replaceAll(word2, "2");
					int ctr=0;
					for(char c : r_str_ctr.toCharArray()) {
						if(c=='2')
							ctr++;
					}
					iSet.put(word2,ctr);
				}					
			}
			
			rtn.put(word, iSet);
		}
		return rtn;
	}
	
	public static Map<String, Integer> getTopWords(String corpus) {
		Map<Integer, List<NGram>> ngrams = NGramMath.generateGramsInRange(corpus, 1, corpus.length());
		
		//get ngram count
		Map<String, Integer> ngramCount = new HashMap<String, Integer>();
		for(int i : ngrams.keySet()) {
					
			for(NGram n : ngrams.get(i)) {
				if(ngramCount.containsKey(n.compareStr())) {
					ngramCount.put(n.compareStr(), ngramCount.get(n.compareStr())+1);
				}
				else {
					ngramCount.put(n.compareStr(), 1);
				}
			}
		}
		
		//prune ngrams that are lower if 
		//there is a higher order ngram that contains them and has the same count
		for(int x=1; x < ngrams.size(); x++) {
			
			List<NGram> masterList = ngrams.get(x);
			
			//prune all under me
			for(int y=1; y < x; y++) {
				
				List<NGram> slaveList = ngrams.get(y);
				
				//create remove list
				List<NGram> toRemove = new ArrayList<NGram>();
				for(NGram slave : slaveList) {
					for(NGram master : masterList) {
						boolean contains = master.compareStr().indexOf(slave.compareStr()) > -1;
						boolean sameCount = ngramCount.get(slave.compareStr())==ngramCount.get(master.compareStr());
						boolean notAlreadyContains = !toRemove.contains(slave);
						if(contains && sameCount && notAlreadyContains) {
							toRemove.add(slave);
							//System.out.println("PRUNE: " + slave.compareStr() + " " + master.compareStr());
						}
					}
				}
				
				//remove
				for(NGram remove : toRemove) {
					slaveList.remove(remove);
				}
			}			
		}
		
		List<String> words = new ArrayList<String>();
		Map<String, Integer> rtn = new LinkedHashMap<String,Integer>();
		for(int i : ngrams.keySet()) {
			
			List<NGram> list = ngrams.get(i);
			for(NGram n : list) {
				if(ngramCount.get(n.compareStr())>1 && !words.contains(n.compareStr())) //not contain and not one occurence
				{
					words.add(n.compareStr());
					rtn.put(n.compareStr(), ngramCount.get(n.compareStr()));
					System.out.println(n.compareStr() + " " + ngramCount.get(n.compareStr()));
				}
			}
		}
		System.out.println("NUMWORDS: " + words.size());	
		return rtn;
	}
	
	
	public static List<List<String>> getSpanningSets(String corpus) {
		Map<Integer, List<NGram>> ngrams = NGramMath.generateGramsInRange(corpus, 1, corpus.length());
		
		//get ngram count
		Map<String, Integer> ngramCount = new HashMap<String, Integer>();
		for(int i : ngrams.keySet()) {
			if(i==1)
				continue;
			
			for(NGram n : ngrams.get(i)) {
				if(ngramCount.containsKey(n.compareStr())) {
					ngramCount.put(n.compareStr(), ngramCount.get(n.compareStr())+1);
				}
				else {
					ngramCount.put(n.compareStr(), 1);
				}
			}
		}
		
		//prune ngrams that are lower if 
		//there is a higher order ngram that contains them and has the same count
		for(int x=1; x < ngrams.size(); x++) {
			
			List<NGram> masterList = ngrams.get(x);
			
			//prune all under me
			for(int y=1; y < x; y++) {
				
				List<NGram> slaveList = ngrams.get(y);
				
				//create remove list
				List<NGram> toRemove = new ArrayList<NGram>();
				for(NGram slave : slaveList) {
					for(NGram master : masterList) {
						
						if(master.compareStr().indexOf(slave.compareStr()) > -1 && ngramCount.get(slave.compareStr())==ngramCount.get(master.compareStr())) {
							toRemove.add(slave);
						}
					}
				}
				
				//remove
				for(NGram remove : toRemove) {
					slaveList.remove(remove);
				}
			}			
		}
		
		//initial library is ngrams of size 1
		List<List<String>> rtn = new ArrayList<List<String>>();
		List<NGram> oneGramsObj = ngrams.get(1);
		List<String> oneGrams = new ArrayList<String>();
		for(NGram n : oneGramsObj) {
			oneGrams.add(n.compareStr());
		}
		rtn.add(oneGrams);
		
		List<String> words = new ArrayList<String>();
		for(int i : ngrams.keySet()) {
			if(i==1)
				continue;
			
			List<NGram> list = ngrams.get(i);
			for(NGram n : list) {
				if(ngramCount.get(n.compareStr())>1 && !words.contains(n.compareStr())) //not contain and not one occurence
					words.add(n.compareStr());
			}
		}
		System.out.println("NUMWORDS: " + words.size());
		
		for(String word : words) {
//			System.out.println(word);
			List<List<String>> spanSets = getSpanningSets_perNewWord(rtn,word);
			for(List<String> spanSet : spanSets) {
				if(!contains(rtn,spanSet)) {
					rtn.add(spanSet);
				}
			}
	//		System.out.println(rtn.size());
		}
		
		return rtn;		
	}
	
	public static boolean contains(List<List<String>> master, List<String> newbie) {
		String n_str = SpanningSetMath.getSpanSetStr(newbie);
		for(List<String> m : master) {
			String m_str = SpanningSetMath.getSpanSetStr(m);
			if(m_str.equalsIgnoreCase(n_str))
				return true;
		}
		return false;
	}
		
	public static List<List<String>> getSpanningSets_perNewWord(List<List<String>> pastSets, String newWord) {
		List<List<String>> rtn = new ArrayList<List<String>>();
		
		for(List<String> set  : pastSets) {
			
			//replace any number of occurs between two elements of word
			List<Occurence> occurences = getOccurences(set, newWord);
			List<List<Occurence>> oSets = getConsistentOccurenceSets(occurences);
			for(List<Occurence> transforms : oSets) {
				List<String> transformedSet = applyTransforms(set, transforms, newWord);
				rtn.add(transformedSet);
			}
		}
		
		return rtn;
	}
	
	public static List<String> applyTransforms(List<String> set, List<Occurence> transforms, String word) {
		List<String> rtn = new ArrayList<String>();
		
		//create string wrappers
		List<StringWrapper> wrapperSet = new ArrayList<StringWrapper>();
		for(String str : set) {
			wrapperSet.add(new StringWrapper(str));
		}
		
		//to remove
		List<StringWrapper> toRemove = new ArrayList<StringWrapper>();
		
		//apply transforms
		for(Occurence transform : transforms) {
			int start = transform.getX();
			int end = transform.getY();
			wrapperSet.set(start, new StringWrapper(word));
			
			//add dead elements to remove list
			for(int x=start+1; x <= end; x++) {
				toRemove.add(wrapperSet.get(x));
			}
		}
		
		//apply removes
		for(StringWrapper r : toRemove) {
			wrapperSet.remove(r);
		}
		
		//create rtn
		for(StringWrapper r : wrapperSet) {
			rtn.add(r.getStr());
		}
		
		return rtn;
	}
		
	public static List<List<Occurence>> getConsistentOccurenceSets(List<Occurence> occurences) {
		List<List<Occurence>> rtn = new ArrayList<List<Occurence>>();
		
		List<String> masks = generateMasks((int)Math.pow(2, occurences.size()));
		for(String mask : masks) {
			List<Occurence> oSet = generateOccurenceSet(occurences, mask);
			if(isConsistentOccurenceSet(oSet)) {
				rtn.add(oSet);
			}
		}
		
		return rtn;
	}
	
	public static List<Occurence> generateOccurenceSet(List<Occurence> occurences, String mask) {
		List<Occurence> rtn = new ArrayList<Occurence>();
		for(int x=0; x < mask.length(); x++) {
			char c  = mask.charAt(x);
			if(c=='1') {
				rtn.add(occurences.get(x));
			}
		}
		return rtn;
	}
	
	public static boolean isConsistentOccurenceSet(List<Occurence> occurences) {
		for(Occurence o : occurences) {
			for(Occurence b : occurences) {
				if(o==b)
					continue;
				if(!posI(o,b))
					return false;
			}
		}
		return true;
	}
	
	public static List<String> generateMasks(int n) {
		List<String> rtn = new ArrayList<String>();
		int maxDigits = Integer.toBinaryString(n-1).length();
		for(int x=n-1; x < n; x++) {
			rtn.add(pad(Integer.toBinaryString(x),maxDigits));
		}
		return rtn;
	}
	
	public static String pad(String str, int l) {
		int numInject = l - str.length();
		String rtn = str;
		if(numInject > 0) {
			for(int x=0; x < numInject; x++)
				rtn = "0" + rtn;
		}
		return rtn;
	}
	
	public static List<Occurence> getOccurences(List<String> set, String word) {
		List<Occurence> rtn = new ArrayList<Occurence>();
		for(int z=1; z <= word.length(); z++) {
			int length = z;
			for(int x=0; x <= set.size(); x++) {
				
				if((x+length) <= set.size()) {
					
					//construct test
					String test="";
					for(int y=x; y < (x+length); y++) {
						test+= set.get(y);
					}
					
					//test
					if(word.equalsIgnoreCase(test)) {
						Occurence o = new Occurence(x,x+length-1);
						rtn.add(o);
					}				
				}
			}
		}
		
		return rtn;
	}
	
	public static boolean posI(Occurence n1, Occurence n2) {
		return !intersect(n1,n2);
	}
	
	public static boolean intersect(Occurence n1, Occurence n2) {
		
		boolean cond1 = n2.getY() >= n1.getX() && n2.getX() <= n1.getX();
		boolean cond2 = n2.getX() <= n1.getY() && n2.getY() >= n1.getY();
		return cond1 || cond2;
	}

	public static String getSpanSetStr(List<String> set) {
		StringBuffer rtn = new StringBuffer();
		rtn.append("{");
		for(int x=0; x < set.size(); x++) {
			String str = set.get(x);
			if(x!=set.size()-1)
				rtn.append(str + ",");
			else 
				rtn.append(str);
		}
		rtn.append("}");
		return rtn.toString();
	}

	public static String getTableStr(Map<String,Integer> set) {
		StringBuffer rtn = new StringBuffer();
		rtn.append("{");
		for(String str : set.keySet()) {
			rtn.append(str + "(" +set.get(str)+"),");
		}
		rtn.append("}");
		return rtn.toString();
	}

}
