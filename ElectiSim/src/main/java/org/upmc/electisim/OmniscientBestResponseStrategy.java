package org.upmc.electisim;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class OmniscientBestResponseStrategy extends ABestReponseAgentStrategy {

	public OmniscientBestResponseStrategy(StateBuffer buffer, IVotingRule rule) {
		super(buffer, rule);
	}

	@Override
	public VoteResult executeVote(Agent agent, List<Candidate> candidateList, int committeeSize) {
		List<List<Candidate>> possibleCommittees = generateCombinations(candidateList, committeeSize);	
		SimulationState state = buffer.getCurrent();
		PreferenceType type = agent.getPreferences().getType();
		
		List<VoteResult> results = new ArrayList<>();
		int agentIdx = 0;
		for(int i = 0; i < results.size(); i++) {
			if(results.get(i).getAgent().equals(agent)) {
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
			
			ElectionResult electionResult = rule.getElectionResult(results, committeeSize);
			Optional<Integer> dist = agent.getPreferences().getCommitteeDistance(electionResult.getElectedCommittee());
			if(dist.get() < bestDist) {
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
		return generateCombinationsAux(candidateList, 0, (candidateList.size() - committeeSize), committeeSize, new ArrayList<List<Candidate>>(), new ArrayList<Candidate>());
	}
	
	private List<List<Candidate>> generateCombinationsAux(List<Candidate> candidateList, int begin, int end, int level, List<List<Candidate>> l, List<Candidate> tmp) {
		if(level == 0) {
			l.add(tmp);
		}
		
		for(int i = begin; i < end; i++) {
			tmp.set(level - 1, candidateList.get(i));
			generateCombinationsAux(candidateList, begin + 1, end + 1, level - 1, l, new ArrayList<Candidate>(tmp));
		}
		
		return l;
	}

}
