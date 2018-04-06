package org.upmc.electisim;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class BlocVotingRule implements IVotingRule {

	@Override
	public List<Candidate> getElectedCommittee(List<VoteResult> results, int committeeSize) {
		Map<Candidate, Integer> scores = new HashMap<>();
		List<Candidate> electedCommittee = new ArrayList<>();
		
		for(VoteResult res : results) {
			for(Map.Entry<Candidate, Integer> entry : res.getScoreMap().entrySet()) {
				Candidate c = entry.getKey();
				if(!scores.containsKey(c)) {
					scores.put(c, 0);
				}
				else {
					scores.put(c, scores.get(c).intValue() + entry.getValue().intValue());
				}
			}
		}
		
		List<Map.Entry<Candidate, Integer>> set = new ArrayList<>(scores.entrySet());
		set.sort(Map.Entry.comparingByValue(new Comparator<Integer>() {

			@Override
			public int compare(Integer arg0, Integer arg1) {
				int arg0Val = arg0.intValue();
				int arg1Val = arg1.intValue();
				
				if(arg0Val > arg1Val) {
					return arg0Val;
				}
				return arg1Val;
			}
			
		}));
		
        for(Map.Entry<Candidate, Integer> c : set) {
        	electedCommittee.add(c.getKey());
        }
        
        return electedCommittee;
	}

}
