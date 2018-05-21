package org.upmc.electisim;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.upmc.electisim.utils.MapUtils;

public class ChamberlinCourantVotingRule implements IVotingRule {

	List<Committee> committeeListCache;
	
	
	public ChamberlinCourantVotingRule() {
		committeeListCache = null;
	}
	
	
	@Override
	public ElectionResult getElectionResult(List<AgentVote> results, int committeeSize) {
		
		System.out.println("*******************Chamberlin Courant Voting Rule******************");
		//TODO : we need the list of candidates
		//ArrayList<Candidate> candidateList = new ArrayList<>();
		
		if(committeeListCache == null){
			committeeListCache = generateCombinations(results.get(0).getLinearOrder(), committeeSize);
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
	

	private List<Committee> generateCombinations(List<IElectable> candidateList, int committeeSize) {
		List<Candidate> tmp = new ArrayList<>();
		for(int i = 0; i < committeeSize; i++) {
			tmp.add(null);
		}
		return generateCombinationsAux(candidateList, 0, candidateList.size(), committeeSize, new ArrayList<Committee>(), tmp);
	}
	
	private List<Committee> generateCombinationsAux(List<IElectable> candidateList, int begin, int end, int level, List<Committee> l, List<Candidate> tmp) {
		if(level == 0) {
			l.add(new Committee(tmp));
			return l;
		}
		
		for(int i = begin; i <= (end - level); i++) {
			tmp.set(level - 1, (Candidate) candidateList.get(i));
			generateCombinationsAux(candidateList, i + 1, end, level - 1, l, new ArrayList<Candidate>(tmp));
		}
		
		return l;
	}

}
