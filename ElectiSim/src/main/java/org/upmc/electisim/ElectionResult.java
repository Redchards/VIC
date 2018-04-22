package org.upmc.electisim;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.upmc.electisim.utils.MapUtils;

public class ElectionResult {
	private final Map<Candidate, Integer> scoreMap;
	private final List<Candidate> electedCommittee;
	
	public ElectionResult(Map<Candidate, Integer> scoreMap, List<Candidate> electedCommittee) {
		this.scoreMap = scoreMap;
		this.electedCommittee = electedCommittee;
	}
	
	public int getCandidateScore(Candidate candidate) {
		return scoreMap.get(candidate);
	}
	
	public Iterator<Candidate> candidateIterator() {
		return scoreMap.keySet().iterator();
	}
	
	public List<Candidate> generateAscendingCandidateRanking() {
		List<Map.Entry<Candidate, Integer>> sortedSet = MapUtils.sortByValue(scoreMap);
		List<Candidate> res = new ArrayList<>();
		
		for(Map.Entry<Candidate, Integer> entry : sortedSet) {
			res.add(entry.getKey());
		}
		
		return res;
	}
	
	public List<Candidate> generateDescendingCandidateRanking() {
		List<Candidate> res = this.generateAscendingCandidateRanking();
		Collections.reverse(res);
		
		return res;
	}
	
	public List<Candidate> getElectedCommittee() {
		return Collections.unmodifiableList(electedCommittee);
	}
	
	@Override
	public boolean equals(Object obj) {
		ElectionResult other = (ElectionResult) obj;
		
		return this.scoreMap.equals(other.scoreMap) && this.electedCommittee.equals(other.electedCommittee);
	}
}
