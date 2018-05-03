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
	public ElectionResult getElectionResult(List<VoteResult> results, int committeeSize) {
		Map<Candidate, Integer> scores = new HashMap<>();
		List<Candidate> electedCommittee = new ArrayList<>();
		
		for(VoteResult res : results) {
			for(Map.Entry<Candidate, Integer> entry : res.getScoreMap().entrySet()) {
				Candidate c = entry.getKey();
				if(!scores.containsKey(c)) {
					scores.put(c, entry.getValue().intValue());
				}
				else {
					scores.put(c, scores.get(c).intValue() + entry.getValue().intValue());
				}
			}
		}
		
		List<Map.Entry<Candidate, Integer>> set = MapUtils.sortByValue(scores);
		Collections.reverse(set);
		
		
        for(Map.Entry<Candidate, Integer> c : set.subList(0, committeeSize)) {
        	electedCommittee.add(c.getKey());
        }
        
        System.out.println(scores.toString());
                
        return new ElectionResult(scores, electedCommittee);
	}

}
