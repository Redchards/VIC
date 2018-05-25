package org.upmc.electisim;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.upmc.electisim.utils.MapUtils;

/**
 * <p>A class implementing the K-approval voting rule, one of the simplest voting rules found
 * in the literature. The scoring function is simple : it takes a {@link org.upmc.electisim.AgentVote}
 * and uses a linear order based on the scores, then selects the k bests of this linear order.
 * Every agent in this set sees its score incremented by 1 point. The winning committee is
 * the k agents with the highest scores.cThe tie breaking is a simple lexicographic one.</p>
 * <p>Despite its simplicity, this voting rule is already pretty efficient when use in 
 * conjunction with responsive preferences</p>
 * @see org.upmc.electisim.PreferenceType
 */
public class BlocVotingRule implements IVotingRule {

	/*
	 * (non-Javadoc)
	 * Generate the election result from the agents' votes
	 * 
	 * @see org.upmc.electisim.IVotingRule#getElectionResult(java.util.List, int)
	 */
	@Override
	public ElectionResult getElectionResult(List<AgentVote> results, int committeeSize) {
		Map<IElectable, Integer> scores = new HashMap<>();
		List<IElectable> electedCommittee = new ArrayList<>();

		for(IElectable c : results.get(0).getScoreMap().keySet()) {
			scores.put(c,  0);
		}

		for(AgentVote res : results) {
			for(IElectable c : res.getKBests(committeeSize)) {
				//System.out.println(res.getAgent().getName()+" voted for "+c.getName()+" with old score : "+scores.get(c).intValue());
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
