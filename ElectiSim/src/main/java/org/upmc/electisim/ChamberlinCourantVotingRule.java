package org.upmc.electisim;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.upmc.electisim.utils.CombinatoricsUtils;
import org.upmc.electisim.utils.MapUtils;

/**
 * <p>A class implementing the Chamberlin-Courant voting rule. This voting rule works
 * on committees instead of singular candidates. For a committee and a given
 * agent vote, we increment this committee's score by the score of the candidate in the 
 * committee having the highest Borda's score in the agent's vote. We repeat this process
 * for every agent vote and every committee. Once every vote has been processed, we select
 * the committee with the highest score</p>
 */
public class ChamberlinCourantVotingRule implements IVotingRule {

	/*
	 * (non-javadoc)
	 * The possible committees of a certain size. Acts as a cache to note regenerate
	 * all the possible committees every time and thus to speed up the process 
	 */
	private List<Committee> committeeListCache;
	
	/*
	 * (non-javadoc)
	 * The last committee size requested. If the size changed, we regenerate the committee cache
	 * @see org.upmc.electisim.ChamberlingCourantVotingRule#committeeListCache
	 */
	private int lastCommitteeSize;
	
	/**
	 * Build the voting rule
	 */
	public ChamberlinCourantVotingRule() {
		committeeListCache = null;
		lastCommitteeSize = 0;
	}
	
	/*
	 * (non-Javadoc)
	 * Generate the election result from the agents' votes
	 * 
	 * @see org.upmc.electisim.IVotingRule#getElectionResult(java.util.List, int)
	 */
	@Override
	public ElectionResult getElectionResult(List<AgentVote> results, int committeeSize) {
		
		System.out.println("*******************Chamberlin Courant Voting Rule******************");
		//TODO : we need the list of candidates
		//ArrayList<Candidate> candidateList = new ArrayList<>();
		
		if(committeeListCache == null || lastCommitteeSize != committeeSize){
			committeeListCache = CombinatoricsUtils.generateCombinations(results.get(0).getLinearOrder(), committeeSize)
									.stream().map((e) -> new Committee(e)).collect(Collectors.toList());
			lastCommitteeSize = committeeSize;
		}
		
		Map<IElectable, Integer> scores = new HashMap<>();
		List<IElectable> electedCommittee = new ArrayList<>();
		
		//init
		for(Committee committee : committeeListCache){
			scores.put(committee, 0);
			//System.out.println(committee.getName());
		}
		
		//committee scores
		for(Committee committee : committeeListCache){
			//System.out.println(committee.getName()+" evaluation : ");
			for(AgentVote res : results){
				scores.put(committee, scores.get(committee).intValue() + getVote(res, committee));
				
//				int score = getVote(res, committee);
//				System.out.println(res.getAgent().getName()+" score : "+score);
			}
		}
		
		
		List<Map.Entry<IElectable, Integer>> set = MapUtils.sortByValue(scores);

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

		electedCommittee.add(set.get(0).getKey());

		
		return new ElectionResult(scores, (List<IElectable>) electedCommittee);
		
	}

	
	
	private int getVote(AgentVote vote, Committee committee) {
		int highestScore = 0;
		for(IElectable c : vote.getScoreMap().keySet()){
			if(committee.getName().contains(c.getName()) && vote.getScoreMap().get(c) > highestScore){
				highestScore = vote.getScoreMap().get(c);
			}
		}
		return highestScore;
	}

}
