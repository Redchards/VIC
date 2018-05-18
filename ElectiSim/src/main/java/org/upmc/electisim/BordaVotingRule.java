package org.upmc.electisim;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.upmc.electisim.utils.MapUtils;

public class BordaVotingRule implements IVotingRule {

	@Override
	public ElectionResult getElectionResult(List<AgentVote> results, int committeeSize) {
		
		System.out.println("******************Borda VR*********************");
		Map<IElectable, Integer> scores = new HashMap<>();
		List<IElectable> electedCommittee = new ArrayList<>();

		//initialize scores
		for(IElectable c : results.get(0).getScoreMap().keySet()) {
			scores.put(c,  0);
		}

		//compute the votes
		for(AgentVote res : results) {
			//System.out.println("Agent : "+res.getAgent().getName()+" order : "+res.getLinearOrder().toString());
			for(IElectable c : res.getScoreMap().keySet()) {
				//System.out.println(res.getAgent().getName()+" voted for "+c.getName()+" with old score : "+scores.get(c).intValue()+" + "+res.getScoreMap().get(c));
				scores.put(c, scores.get(c).intValue() + res.getScoreMap().get(c));
			}
		}
		
		//sort the list of the votes
		List<Map.Entry<IElectable, Integer>> set = MapUtils.sortByValue(scores);
		//Collections.reverse(set);

		set.sort(new Comparator<Map.Entry<IElectable,Integer>>() {

			@Override
			public int compare(Map.Entry<IElectable, Integer> c_score1, Map.Entry<IElectable, Integer> c_score2) {
				int comp_by_value = c_score2.getValue().compareTo(c_score1.getValue());
				if (comp_by_value != 0) {
					return comp_by_value;
				} else {
					return c_score1.getKey().getName().compareTo(c_score2.getKey().getName());
				}
			}
		});

		//get the k first
		for(Map.Entry<IElectable, Integer> c : set.subList(0, committeeSize)) {
			electedCommittee.add(c.getKey());
		}

		return new ElectionResult(scores, electedCommittee);
	}
}
