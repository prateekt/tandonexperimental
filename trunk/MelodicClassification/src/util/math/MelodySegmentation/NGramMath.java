package util.math.MelodySegmentation;
import java.util.*;

import util.math.dist.Gaussian;

public class NGramMath {
	
	public static List<Integer> reverseList(List<Integer> original) {
		List<Integer> rtn = new ArrayList<Integer>();
		for(int x=original.size()-1; x >= 0; x--) {
			rtn.add(original.get(x));
		}
		return rtn;
	}
	
	public static double AVERAGE_SIZE =0;
	public static double AVERAGE_N = 0;
	
	public static List<PossibleTagging> doIt(String corpus, int min_length, int max_length) {
		Map<Integer, List<NGram>> nGramMap = NGramMath.generateGramsInRange(corpus, min_length, max_length);
		Map<Integer, Map<NGram, PossibleTagging>> globalMap = getGlobalTagMap(nGramMap);
		List<PossibleTagging> globalPT = new ArrayList<PossibleTagging>();
		for(int i : globalMap.keySet()) {
			Map<NGram, PossibleTagging> map = globalMap.get(i);
			List<Double> sizeList = new ArrayList<Double>();
			List<Double> n = new ArrayList<Double>();
			for(NGram ng : map.keySet()) {
				PossibleTagging pt = map.get(ng);
				globalPT.add(pt);
				sizeList.add((double)pt.getSize());
				n.add((double)pt.getN());
			}
			Gaussian g = new Gaussian(sizeList);
			Gaussian g2 = new Gaussian(n);
			System.out.println(i + " " + g.getMean() + " " + g.getStddev());
		}
		
		Collections.sort(globalPT);
		List<PossibleTagging> usedTags = new ArrayList<PossibleTagging>();
		boolean[] gridState = new boolean[corpus.length()];
		for(PossibleTagging t : globalPT) {
			if(taggingPossible(gridState,t)) {
				usedTags.add(t);
				tag(gridState, t);
			}
		}
				
		for(PossibleTagging t : usedTags) {
			System.out.println(t);
		}
		
		return usedTags;
	}
		
	public static void tag(boolean[] gridState, PossibleTagging t) {
		
		//do original
		NGram original = t.getOriginal();
		for(int x=original.getSpanMin(); x<= original.getSpanMax(); x++) {
			gridState[x] = true;
		}
		
		//do following elements
		List<NGram> followingElements = t.getFollowingElements();
		for(NGram n : followingElements) {
			for(int x=n.getSpanMin(); x <= n.getSpanMax(); x++) {
				gridState[x] = true;
			}
		}
	}
	
	public static boolean taggingPossible(boolean[] gridState, PossibleTagging t) {
		
		//check original
		NGram original = t.getOriginal();
		for(int x=original.getSpanMin(); x <= original.getSpanMax(); x++) {
			if(gridState[x])
				return false;
		}
		
		//check following elements
		List<NGram> followingElements = t.getFollowingElements();
		for(NGram n : followingElements) {
			for(int x=n.getSpanMin(); x <= n.getSpanMax(); x++) {
				if(gridState[x])
					return false;
			}
		}
		
		return true;
	}
	
	public static Map<Integer, Map<NGram,PossibleTagging>> getGlobalTagMap(Map<Integer, List<NGram>> nGramMap) {
		Map<Integer, Map<NGram, PossibleTagging>> rtn = new LinkedHashMap<Integer, Map<NGram,PossibleTagging>>();
		
		for(int n : nGramMap.keySet()) {
			List<NGram> list = nGramMap.get(n);
			Map<NGram, PossibleTagging> map = generatePossibleTaggings(list);
			rtn.put(n, map);
		}
		
		return rtn;		
	}
	
	public static Map<NGram, PossibleTagging> generatePossibleTaggings(List<NGram> ngrams) {
		Map<NGram, PossibleTagging> rtn = new LinkedHashMap<NGram, PossibleTagging>();
		for(NGram n : ngrams) {
			PossibleTagging p = getPossibleTagging(n, ngrams);
			rtn.put(n, p);
		}
		return rtn;
	}
	
	public static PossibleTagging getPossibleTagging(NGram test, List<NGram> ngrams) {
		
		List<NGram> followingElements = new ArrayList<NGram>();
		for(NGram n : ngrams) {
			
			//if me, its not a free pass :)
			if(test==n)
				continue;
			
			if(tagFunction(test,n)) {
				followingElements.add(n);
			}
		}		
		PossibleTagging rtn = new PossibleTagging(test,followingElements, test.size());
		return rtn;
	}
	
	public static boolean tagFunction(NGram test, NGram other) {
		return posI(test,other) && test.toString().equalsIgnoreCase(other.toString());
	}
	
	public static Map<Integer, List<NGram>> generateGramsInRange(String corpus, int min_length, int max_length) {
		Map<Integer, List<NGram>> rtn = new LinkedHashMap<Integer, List<NGram>>();
		
		for(int x=min_length; x <= max_length; x++) {
			rtn.put(x, generateNGrams(corpus,x));
		}
		
		return rtn;		
	}
	
	public static List<NGram> generateNGrams(String corpus, int n) {
		List<NGram> rtn = new ArrayList<NGram>();
		
		//easy exit
		if(corpus.length() < n)
			return rtn;
		
		loop:for(int x=0; x < corpus.length(); x++) {
			
			//you're done if you can't moreproduce n-grams
			if(x+n > corpus.length())
				break loop;
			
			//else generate it
			List<String> grams = new ArrayList<String>();
			for(int y=x; y < x+n; y++) {
				String gram = ""+corpus.charAt(y);
				grams.add(gram);
			}
			NGram newGram = new NGram(grams,x,x+n-1);
			rtn.add(newGram);
		}
		
		return rtn;		
	}
	
	public static boolean posI(NGram n1, NGram n2) {
		return !intersect(n1,n2);
	}
	
	public static boolean intersect(NGram n1, NGram n2) {
		
		boolean cond1 = n2.getSpanMax() >= n1.getSpanMin() && n2.getSpanMin() <= n1.getSpanMin();
		boolean cond2 = n2.getSpanMin() <= n1.getSpanMax() && n2.getSpanMax() >= n1.getSpanMax();
		return cond1 || cond2;
	}
}
