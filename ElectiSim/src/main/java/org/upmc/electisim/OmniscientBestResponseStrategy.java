package org.upmc.electisim;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.Stack;

import org.upmc.electisim.knowledge.OmniscientKnowledgeDispenser;

public class OmniscientBestResponseStrategy implements IBestResponseAgentStrategy {

	@Override
	public AgentVote executeVote(Agent agent, OmniscientKnowledgeDispenser dispenser, List<Candidate> candidateList, int committeeSize) {
		System.out.println(agent.getName());
		
		if(dispenser.firstIteration()) {
			Map<Candidate, Integer> scoreMap = new HashMap<>();
			List<Candidate> favouriteCommittee = agent.getPreferences().favouriteCommittee(committeeSize);
			for(Candidate c : candidateList) {
				scoreMap.put(c, 0);
			}
			
			for(Candidate c : favouriteCommittee) {
				scoreMap.put(c, 1);
			}
			
			return new AgentVote(agent, scoreMap);
		}
		
		List<List<Candidate>> possibleCommittees = generateCombinations(candidateList, committeeSize);	
		SimulationState state = dispenser.getLastSimulationState();
		
		List<AgentVote> results = new ArrayList<>();
		int agentIdx = 0;
		for(int i = 0; i < state.getVoteResults().size(); i++) {
			if(state.getVoteResult(i).getAgent().equals(agent)) {
				agentIdx = i;
			}
			results.add(state.getVoteResult(i));
		}
		
		AgentVote blankVoteResult = new AgentVote(results.get(agentIdx));
		for(Candidate c : candidateList) {
			blankVoteResult.setScore(c, 0);
		}
		
		List<Candidate> currentBestCommittee = new ArrayList<>();
		int bestDist = -1;
		List<Candidate> origOrder = results.get(agentIdx).getLinearOrder();
		
		for(List<Candidate> committee : possibleCommittees) {
			results.set(agentIdx, new AgentVote(blankVoteResult));
			
			List<List<Candidate>> permutations = this.generatePermutations(committee);
			for(List<Candidate> permutation : permutations) {
				List<Candidate> currentOrder = new ArrayList<>(origOrder);
				int currentScore = candidateList.size();
				
				for(Candidate c : permutation) {
					results.get(agentIdx).setScore(c, currentScore);
					currentScore--;
					currentOrder.remove(c);
				}
				for(Candidate c : currentOrder) {
					results.get(agentIdx).setScore(c, currentScore);
					currentScore--;
				}
				
				ElectionResult electionResult = dispenser.getVotingRule().getElectionResult(results, committeeSize);
				int dist = agent.getPreferences().getCommitteeDistance(electionResult.getElectedCommittee());
				if(bestDist == -1 
						|| (dist <= bestDist && agent.getPreferences().getCommitteeDistance(committee) < agent.getPreferences().getCommitteeDistance(currentBestCommittee))) {
					currentBestCommittee = permutation;
					// System.out.println(committee);
					bestDist = dist;
				}
			}
		}
		
		//System.out.println("Agent " + agent.getName() + " : " + currentBestCommittee.toString() + " : " + Integer.toString(bestDist));
		
		Map<Candidate, Integer> scoreMap = new HashMap<>();
		
		for(Candidate c : candidateList) {
			scoreMap.put(c, 0);
		}
		
		for(Candidate c : currentBestCommittee) {
			scoreMap.put(c, 1);
		}
		
		return new AgentVote(agent, scoreMap);
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
	
	private List<List<Candidate>> generatePermutations(List<Candidate> committee) {
		Stack<Candidate> stack = new Stack<>();
		List<List<Candidate>> res = new ArrayList<>();
		
		generatePermutationAux(new HashSet<>(committee), stack, res);
		
		return res;
	}
	
	private void generatePermutationAux(Set<Candidate> committee, Stack<Candidate> stack, List<List<Candidate>> res) {
		if(committee.isEmpty()) {
			res.add(Arrays.asList(stack.toArray(new Candidate[0])));
		}
		
		Candidate[] availableItems = committee.toArray(new Candidate[0]);
		for(Candidate item : availableItems) {
			stack.push(item);
			committee.remove(item);
			generatePermutationAux(committee, stack, res);
			committee.add(stack.pop());
		}
	}

}
