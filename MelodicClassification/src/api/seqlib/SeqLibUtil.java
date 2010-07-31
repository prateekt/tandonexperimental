package api.seqlib;
import java.util.*;

public class SeqLibUtil {
	
	public static void main(String[] args) {
		List<LCSAlignment> list = getLCSAlignment("ABABCDC", "ABC",-1);
		for(LCSAlignment l : list) {
			System.out.println(l);
		}
	}	
		
	public static List<LCSAlignment> getLCSAlignment(String s1, String s2, int lastEnd) {
		LongestCommonSubsequence lcs1 = new LongestCommonSubsequence(s1, s2);
		String lcs = lcs1.getLongestCommonSubsequence();
		List<Integer> s1Indicies = lcs1.getSeqIndex();
				
		LongestCommonSubsequence lcs2 = new LongestCommonSubsequence(s2, s1);

		lcs = lcs2.getLongestCommonSubsequence();
		System.out.println(lcs);

		List<Integer> s2Indicies = lcs2.getSeqIndex();
		
		//align
		List<LCSAlignment> rtn = new ArrayList<LCSAlignment>();
		for(int x=s1Indicies.size()-1; x >= 0; x--) {
			
			char c = s1.charAt(x);
			int s1Index = s1Indicies.get(x);
			int s2Index = s2Indicies.get(x);
			System.out.println(s1Index);
			LCSAlignment toAdd = new LCSAlignment(lcs,c,s1Index,s2Index);
			rtn.add(toAdd);			
		}
		for(int x=0; x < lcs.length(); x++) {
			char c = lcs.charAt(x);
			rtn.get(x).setC(c);
		}
		
		//move chars back if earlier -- LCS traditionally returns last last char
		//we want closest S2 Index
/*		int lastIndex = -1;
		for(int x=0; x < lcs.length(); x++) {
			char c = lcs.charAt(x);
			LCSAlignment currentAlignment = rtn.get(x);
			System.out.println(currentAlignment);
			System.out.println(s1);
			String substring = s1.substring(lastIndex+1, currentAlignment.getS1Index()+1);
			int i = substring.indexOf(c);
			if((i+lastIndex+1)==currentAlignment.getS1Index()) {
				lastIndex = currentAlignment.getS1Index();
			}
			else {
				currentAlignment.setS1Index(i+1+lastIndex);
				lastIndex = i;
			}			
		}*/
				
		return rtn;
	}	
}
