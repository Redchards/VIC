package org.upmc.electisim;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
	
	public List<Candidate> getElectedCommittee() {
		return Collections.unmodifiableList(electedCommittee);
	}
}
