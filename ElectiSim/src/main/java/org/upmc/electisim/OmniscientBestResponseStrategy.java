package org.upmc.electisim;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.upmc.electisim.knowledge.OmniscientKnowledgeDispenser;

public class OmniscientBestResponseStrategy implements IBestResponseAgentStrategy {

	@Override
	public VoteResult executeVote(Agent agent, OmniscientKnowledgeDispenser dispenser, List<Candidate> candidateList, int committeeSize) {
		if(dispenser.firstIteration()) {
			Map<Candidate, Integer> scoreMap = new HashMap<>();
			List<Candidate> favouriteCommittee = agent.getPreferences().favouriteCommittee(committeeSize);
			for(Candidate c : candidateList) {
				scoreMap.put(c, 0);
			}
			
			for(Candidate c : favouriteCommittee) {
				scoreMap.put(c, 1);
			}
			
			return new VoteResult(agent, scoreMap);
		}
		
		List<List<Candidate>> possibleCommittees = generateCombinations(candidateList, committeeSize);	
		SimulationState state = dispenser.getLastSimulationState();
		PreferenceType type = agent.getPreferences().getType();
		
		List<VoteResult> results = new ArrayList<>();
		int agentIdx = 0;
		for(int i = 0; i < state.getVoteResults().size(); i++) {
			if(state.getVoteResult(i).getAgent().equals(agent)) {
				agentIdx = i;
			}
			results.add(state.getVoteResult(i));
		}
		
		VoteResult blankVoteResult = results.get(agentIdx);
		for(Candidate c : candidateList) {
			blankVoteResult.setScore(c, 0);
		}
		
		List<Candidate> currentBestCommittee = new ArrayList<>();
		int bestDist = -1;
		
		for(List<Candidate> committee : possibleCommittees) {
			results.set(agentIdx, new VoteResult(blankVoteResult));

			for(Candidate c : committee) {
				results.get(agentIdx).setScore(c, 1);
			}
			
			ElectionResult electionResult = dispenser.getVotingRule().getElectionResult(results, committeeSize);
			Optional<Integer> dist = agent.getPreferences().getCommitteeDistance(electionResult.getElectedCommittee());
			if(bestDist == -1 || dist.get() < bestDist) {
				currentBestCommittee = committee;
				bestDist = dist.get();
			}
		}
		
		Map<Candidate, Integer> scoreMap = new HashMap<>();
		
		for(Candidate c : candidateList) {
			scoreMap.put(c, 0);
		}
		
		for(Candidate c : currentBestCommittee) {
			scoreMap.put(c, 1);
		}
		
		return new VoteResult(agent, scoreMap);
	}
	
	private List<List<Candidate>> generateCombinations(List<Candidate> candidateList, int committeeSize) {
		List<Candidate> tmp = new ArrayList<>();
		for(int i = 0; i < committeeSize; i++) {
			tmp.add(null);
		}
		return generateCombinationsAux(candidateList, 0, candidateList.size(), committeeSize, new ArrayList<List<Candidate>>(), tmp);
	}
	
	private List<List<Candidate>> generateCombinationsAux(List<Candidate> candidateList, int begin, int end, int level, List<List<Candidate>> l, List<Candidate> tmp) {
		if(level == 0) {
			l.add(tmp);
			return l;
		}
		
		for(int i = begin; i <= (end - level); i++) {
			tmp.set(level - 1, candidateList.get(i));
			generateCombinationsAux(candidateList, i + 1, end, level - 1, l, new ArrayList<Candidate>(tmp));
		}
		
		return l;
	}

}
