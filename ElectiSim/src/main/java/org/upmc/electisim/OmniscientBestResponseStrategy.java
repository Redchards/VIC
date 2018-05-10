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
	public AgentVote executeVote(Agent agent, OmniscientKnowledgeDispenser dispenser, List<IElectable> candidateList, int committeeSize) {
		System.out.println(agent.getName());
		
		if(dispenser.firstIteration()) {
			Map<IElectable, Integer> scoreMap = new HashMap<>();
			List<IElectable> favouriteCommittee = agent.getPreferences().favouriteCommittee(committeeSize);
			for(IElectable c : candidateList) {
				scoreMap.put(c, 0);
			}
			
			for(IElectable c : favouriteCommittee) {
				scoreMap.put(c, 1);
			}
			
			return new AgentVote(agent, scoreMap);
		}
		
		List<List<IElectable>> possibleCommittees = generateCombinations(candidateList, committeeSize);	
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
		for(IElectable c : candidateList) {
			blankVoteResult.setScore(c, 0);
		}
		
		List<IElectable> currentBestCommittee = new ArrayList<>();
		int bestDist = -1;
		List<IElectable> origOrder = results.get(agentIdx).getLinearOrder();
		
		for(List<IElectable> committee : possibleCommittees) {
			results.set(agentIdx, new AgentVote(blankVoteResult));
			
			List<List<IElectable>> permutations = this.generatePermutations(committee);
			for(List<IElectable> permutation : permutations) {
				List<IElectable> currentOrder = new ArrayList<>(origOrder);
				int currentScore = candidateList.size();
				
				for(IElectable c : permutation) {
					results.get(agentIdx).setScore(c, currentScore);
					currentScore--;
					currentOrder.remove(c);
				}
				for(IElectable c : currentOrder) {
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
		
		Map<IElectable, Integer> scoreMap = new HashMap<>();
		
		for(IElectable c : candidateList) {
			scoreMap.put(c, 0);
		}
		
		for(IElectable c : currentBestCommittee) {
			scoreMap.put(c, 1);
		}
		
		return new AgentVote(agent, scoreMap);
	}
	
	private List<List<IElectable>> generateCombinations(List<IElectable> candidateList, int committeeSize) {
		List<IElectable> tmp = new ArrayList<>();
		for(int i = 0; i < committeeSize; i++) {
			tmp.add(null);
		}
		return generateCombinationsAux(candidateList, 0, candidateList.size(), committeeSize, new ArrayList<List<IElectable>>(), tmp);
	}
	
	private List<List<IElectable>> generateCombinationsAux(List<IElectable> candidateList, int begin, int end, int level, List<List<IElectable>> l, List<IElectable> tmp) {
		if(level == 0) {
			l.add(tmp);
			return l;
		}
		
		for(int i = begin; i <= (end - level); i++) {
			tmp.set(level - 1, candidateList.get(i));
			generateCombinationsAux(candidateList, i + 1, end, level - 1, l, new ArrayList<IElectable>(tmp));
		}
		
		return l;
	}
	
	private List<List<IElectable>> generatePermutations(List<IElectable> committee) {
		Stack<IElectable> stack = new Stack<>();
		List<List<IElectable>> res = new ArrayList<>();
		
		generatePermutationAux(new HashSet<>(committee), stack, res);
		
		return res;
	}
	
	private void generatePermutationAux(Set<IElectable> committee, Stack<IElectable> stack, List<List<IElectable>> res) {
		if(committee.isEmpty()) {
			res.add(Arrays.asList(stack.toArray(new IElectable[0])));
		}
		
		IElectable[] availableItems = committee.toArray(new IElectable[0]);
		for(IElectable item : availableItems) {
			stack.push(item);
			committee.remove(item);
			generatePermutationAux(committee, stack, res);
			committee.add(stack.pop());
		}
	}

}
