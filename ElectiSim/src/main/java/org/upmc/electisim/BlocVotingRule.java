package org.upmc.electisim;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.upmc.electisim.utils.MapUtils;

public class BlocVotingRule implements IVotingRule {

	@Override
	public ElectionResult getElectionResult(List<AgentVote> results, int committeeSize) {
		Map<Candidate, Integer> scores = new HashMap<>();
		List<Candidate> electedCommittee = new ArrayList<>();
		
		for(Candidate c : results.get(0).getScoreMap().keySet()) {
			scores.put(c,  0);
		}
		
		for(AgentVote res : results) {
			for(Candidate c : res.getKBests(committeeSize)) {
				scores.put(c, scores.get(c).intValue() + 1);
			}
		}
		
		List<Map.Entry<Candidate, Integer>> set = MapUtils.sortByValue(scores);
		Collections.reverse(set);
		
		
        for(Map.Entry<Candidate, Integer> c : set.subList(0, committeeSize)) {
        	electedCommittee.add(c.getKey());
        }
        
        return new ElectionResult(scores, electedCommittee);
	}

}
