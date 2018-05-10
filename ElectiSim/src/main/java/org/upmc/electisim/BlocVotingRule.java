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
		Map<IElectable, Integer> scores = new HashMap<>();
		List<IElectable> electedCommittee = new ArrayList<>();

		for(IElectable c : results.get(0).getScoreMap().keySet()) {
			scores.put(c,  0);
		}

		for(AgentVote res : results) {
			for(IElectable c : res.getKBests(committeeSize)) {
				scores.put(c, scores.get(c).intValue() + 1);
			}
		}

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

		for(Map.Entry<IElectable, Integer> c : set.subList(0, committeeSize)) {
			electedCommittee.add(c.getKey());
		}

		return new ElectionResult(scores, electedCommittee);
	}

}
