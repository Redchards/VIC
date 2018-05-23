package org.upmc.electisim;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.upmc.electisim.utils.MapUtils;

public class ElectionResult {
	private final Map<IElectable, Integer> scoreMap;
	private final List<IElectable> electedCommittee;
	
	public ElectionResult(Map<IElectable, Integer> scoreMap, List<IElectable> electedCommittee) {
		this.scoreMap = scoreMap;
		this.electedCommittee = electedCommittee;
	}
	
	public int getCandidateScore(IElectable candidate) {
		return scoreMap.get(candidate);
	}
	
	public Iterator<IElectable> candidateIterator() {
		return scoreMap.keySet().iterator();
	}
	
	public List<IElectable> generateAscendingCandidateRanking() {
		List<Map.Entry<IElectable, Integer>> sortedSet = MapUtils.sortByValue(scoreMap);
		List<IElectable> res = new ArrayList<>();
		
		for(Map.Entry<IElectable, Integer> entry : sortedSet) {
			res.add(entry.getKey());
		}
		
		return res;
	}
	
	public List<IElectable> generateDescendingCandidateRanking() {
		List<IElectable> res = this.generateAscendingCandidateRanking();
		Collections.reverse(res);
		
		return res;
	}
	
	public List<IElectable> getElectedCommittee() {
		return Collections.unmodifiableList(electedCommittee);
	}
	
	//TODO : not working with Committee
	@Override
	public boolean equals(Object obj) {
		ElectionResult other = (ElectionResult) obj;
//		System.out.println(this.scoreMap.toString()+"\n compared to \n"+other.scoreMap.toString());
//		System.out.println(this.scoreMap.equals(other.scoreMap)+" elected : "+this.electedCommittee.equals(other.electedCommittee));
		return this.scoreMap.equals(other.scoreMap) && this.electedCommittee.equals(other.electedCommittee);
	}
}
