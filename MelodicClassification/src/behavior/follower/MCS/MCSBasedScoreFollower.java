package behavior.follower.MCS;

import java.util.*;

import util.math.StringMath;

import behavior.transcriber.base.Melody;
import behavior.transcriber.base.Note;
import api.seqlib.LCSAlignment;
import api.seqlib.LongestCommonSubsequence;
import api.seqlib.SeqLibUtil;
import api.tuxguitar.MelodyExtractor.TGMelodyExtractor;

public class MCSBasedScoreFollower {
	
	public static void main(String[] args) {
				
		Map<String, List<Melody>> melodies = TGMelodyExtractor.extractMelodies("db/db_gtp/jm.gp4",0,0,0);
		String it = "";
		for(String key : melodies.keySet()) {
			it = key;
		}
		List<Melody> track0Melodies = melodies.get(it);
		track0Melodies = TGMelodyExtractor.getUniqueMelodies(track0Melodies);
		
		//construct stream
		Melody orig = track0Melodies.get(0);
		List<Note> stream = new ArrayList<Note>();
		for(int x=0; x < 2; x++) {
			for(Note n : orig.getNotes()) {
				stream.add(n);
			}
		}

		Melody streamMel =new Melody(stream, "STREAM");
		System.out.println(streamMel);	
		System.out.println(tag(streamMel, track0Melodies));
	}
	
	public static AcceptablePath tag(Melody stream, List<Melody> db) {
		List<MelodyOccurence> list = getMelodyOccurences(stream, db);
		Map<MelodyOccurence, List<MelodyOccurence>> iSets = getIndependenceSets(list);
		List<AcceptablePath> acceptablePaths = new ArrayList<AcceptablePath>();
		for(MelodyOccurence m : iSets.keySet()) {
			expand(iSets, m, new ArrayList<MelodyOccurence>(), acceptablePaths);
		}
				
		return getBestTagging(acceptablePaths);				
	}
	
	public static AcceptablePath getBestTagging(List<AcceptablePath> paths) {
		
		//find best span list
		int maxSpan = Integer.MIN_VALUE;
		for(AcceptablePath p : paths) {
			if(p.getSpanUnits() > maxSpan)
				maxSpan=p.getSpanUnits();
		}
		List<AcceptablePath> bs = new ArrayList<AcceptablePath>();
		for(AcceptablePath p : paths) {
			if(p.getSpanUnits()==maxSpan)  {
				bs.add(p);
			}
		}
		
		//optimize for max num melody
		int maxMelody = Integer.MIN_VALUE;
		for(AcceptablePath p : bs) {
			if(p.getNumMelodies() > maxMelody)
				maxMelody = p.getNumMelodies();
		}
		List<AcceptablePath> bm = new ArrayList<AcceptablePath>();
		for(AcceptablePath p : bs) {
			if(p.getNumMelodies()==maxMelody)
				bm.add(p);
		}
		
		//choose first answer
		if(bm.size() > 0)
			return bm.get(0);
		else
			return null;
	}
	
	public static void expand(Map<MelodyOccurence, List<MelodyOccurence>> map, MelodyOccurence node, List<MelodyOccurence> path, List<AcceptablePath> acceptablePaths) {
		
		if(validPath(path,node,acceptablePaths)) {
						
			//construct new valid path
			List<MelodyOccurence> newPath = new ArrayList<MelodyOccurence>();
			for(MelodyOccurence p : path) {
				newPath.add(p);
			}
			newPath.add(node);
			AcceptablePath aPath = new AcceptablePath(newPath); 
			
			//add to acceptable paths
			acceptablePaths.add(aPath);
			
			//expand all neighbors
			for(MelodyOccurence neighbor : map.get(node)) {
				expand(map, neighbor, newPath, acceptablePaths);
			}						
		}
	}
	
	public static boolean validPath(List<MelodyOccurence> path, MelodyOccurence node, List<AcceptablePath> aPaths) {
		
		//already contains
		if(path.contains(node))
			return false;
		
		//independent of all nodes in path
		for(MelodyOccurence o : path) {			
			if(!independentFollowingSequences(o, node) || !independentFollowingSequences(node,o))
				return false;
		}
		
		//not expanded already
		for(AcceptablePath p : aPaths) {
			
			//same length?
			if(p.getPath().size()!=path.size()+1)
				continue;
			
			//does it contain new node?
			if(!p.getPath().contains(node))
				continue;
			
			//does it contain all nodes in current path?
			for(MelodyOccurence o : path) {
				if(!p.getPath().contains(o))
					continue;
			}
			
			//it is the same!
			return false;
		}
		
		return true;
	}
	
	
	
	public static Map<MelodyOccurence, List<MelodyOccurence>> getIndependenceSets(List<MelodyOccurence> l) {
		Map<MelodyOccurence, List<MelodyOccurence>> rtn = new LinkedHashMap<MelodyOccurence, List<MelodyOccurence>>();
		
		for(MelodyOccurence o : l) {
			List<MelodyOccurence> toAdd = new ArrayList<MelodyOccurence>();
			for(MelodyOccurence o2 : l) {
				if(o2==l)
					continue;
				if(independentFollowingSequences(o,o2)) {
					toAdd.add(o2);
				}
			}
			rtn.put(o, toAdd);
		}
		
		return rtn;
	}
	
	public static List<MelodyOccurence> getMelodyOccurences(Melody stream, List<Melody> lsList) {
		List<MelodyOccurence> rtn = new ArrayList<MelodyOccurence>();
		for(Melody ls : lsList) {
		
			//convert stream to mask
			String streamMask = stream.maskSeqStr();
			String streamMaskGlobal = stream.maskSeqStr();
			
			int x=0;
			int lastEnd =0;
			while(containsSubsequence(streamMask, ls.maskSeqStr())) {
				
				//add occurrence
				List<LCSAlignment> alignment = getAlignment(streamMaskGlobal, ls.maskSeqStr(), lastEnd);
				MelodyOccurence toAdd = new MelodyOccurence(ls, alignment);
				rtn.add(toAdd);
				
				//break if already done
				if(toAdd.getEnd()==streamMaskGlobal.length()-1)
					break;
								
				//else update stream mask
				streamMask = streamMaskGlobal.substring(toAdd.getEnd()+1, streamMaskGlobal.length());
				lastEnd = toAdd.getEnd()+1;
				x++;
			}
		}

		return rtn;		
	}
	
	public List<Melody> getLongestSubsequenceMelodies(Melody stream, List<Melody> mels) {
		//convert stream to mask
		String streamMask = stream.maskSeqStr();
		
		//get containment melodies
		List<Melody> rtn = new ArrayList<Melody>();
		for(Melody m : mels) {
			String maskStr = m.maskSeqStr();
			if(containsSubsequence(streamMask, maskStr)) {
				rtn.add(m);
			}
		}
		
		//prune out shorter melodies
		List<Melody> toRemove = new ArrayList<Melody>();
		for(Melody m : rtn) {
			for(Melody n : rtn) {
				
				if(m==n)
					continue;
				
				if(containsSubsequence(m.maskSeqStr(), n.maskSeqStr())) {
					toRemove.add(n);
				}				
			}
		}
		for(Melody r : toRemove) {
			rtn.remove(r);
		}
		
		return rtn;
	}
	
	public static List<LCSAlignment> getAlignment (String globalCorpus, String subseq, int startIndex) {
		List<LCSAlignment> rtn = new ArrayList<LCSAlignment>();
		
		String substring = globalCorpus.substring(startIndex, globalCorpus.length());
		
		//does it contain subseq?
		if(!containsSubsequence(substring,subseq)) {
			//if not, return empty list
			return rtn;
		}
		else {
			//otherwise extract first and return
			int currentNeedleIndex=0;
			char currentNeedle = subseq.charAt(currentNeedleIndex);
			for(int x=0; x < substring.length(); x++) {
				char currentHay = substring.charAt(x);
				if(currentHay==currentNeedle) {
					LCSAlignment toAdd = new LCSAlignment(substring, currentHay,x+startIndex,x+startIndex);
					rtn.add(toAdd);
					
					//done
					if(currentNeedleIndex==subseq.length()-1)
						break;
					
					currentNeedleIndex++;
					currentNeedle = subseq.charAt(currentNeedleIndex);
				}
			}
		}
		
		return rtn;
	}
	
	public static boolean independentFollowingSequences(MelodyOccurence o1, MelodyOccurence o2) {		
		int M_start = o1.getStart();
		int M_end = o1.getEnd();
		int N_start = o2.getStart();
		int N_end = o2.getEnd();
		
		//intersect conditions		
		boolean cond1 = M_start <= N_end && N_end <= M_end;
		boolean cond2 = M_start <= N_start && N_start <= M_end;
		boolean cond3 = N_start <= M_end && M_end <= N_end;
		boolean cond4 = N_start <= M_start && M_start <= N_end;
		
		return !cond1 && !cond2 && !cond3 && !cond4;
	}
		
	public static boolean containsSubsequence(String corpus, String seq) {
		LongestCommonSubsequence lcs = new LongestCommonSubsequence(corpus,seq);
		String lcsString = lcs.getLongestCommonSubsequence();
		return lcsString.length()==seq.length();
	}
}
