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
import org.upmc.electisim.utils.CombinatoricsUtils;

public class OmniscientBestResponseStrategy implements IBestResponseAgentStrategy {

	@Override
	public AgentVote executeVote(Agent agent, OmniscientKnowledgeDispenser dispenser, List<IElectable> candidateList, int committeeSize) {
		System.out.println(agent.getName());
		
		if(dispenser.firstIteration()) {
			Map<IElectable, Integer> scoreMap = new HashMap<>();
			List<IElectable> favouriteCommittee = agent.getPreferences().favouriteCommittee(committeeSize);
			System.out.println(agent.getName()+" preference list : "+agent.getPreferences().getPreferenceList().toString());
			int currentScore = candidateList.size();
			for(IElectable c : agent.getPreferences().getPreferenceList()){
				scoreMap.put(c, currentScore);
				currentScore--;
			}
			
			return new AgentVote(agent, scoreMap);
		}
		
		List<List<IElectable>> possibleCommittees = CombinatoricsUtils.generateCombinations(candidateList, committeeSize);	
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
		
//		List<IElectable> currentBestCommittee = new ArrayList<>();
//		int bestDist = -1;
		
		List<IElectable> currentBestCommittee = state.getVoteResult(agentIdx).getLinearOrder();
		ElectionResult electionResult = dispenser.getVotingRule().getElectionResult(results, committeeSize);
		int bestDist = agent.getPreferences().getCommitteeDistance(electionResult.getElectedCommittee());
		List<IElectable> origOrder = results.get(agentIdx).getLinearOrder();
		
		for(List<IElectable> committee : possibleCommittees) {
			results.set(agentIdx, new AgentVote(blankVoteResult));
			
			List<List<IElectable>> permutations = CombinatoricsUtils.generatePermutations(committee);
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
				
				electionResult = dispenser.getVotingRule().getElectionResult(results, committeeSize);
				int dist = agent.getPreferences().getCommitteeDistance(electionResult.getElectedCommittee());
				System.out.println("Processing distances when voting for "+permutation+"\n>Elected committee : "+electionResult.getElectedCommittee().toString());
				System.out.println(">distance : "+dist);
				
				if(bestDist == -1 || dist < bestDist) {
					currentBestCommittee = permutation;
					System.out.println("new bestCommittee : "+committee);
					bestDist = dist;
				}
			}
		}
		
		//System.out.println("Agent " + agent.getName() + " : " + currentBestCommittee.toString() + " : " + Integer.toString(bestDist));
		
		
		//Set the Borda scores
		Map<IElectable, Integer> scoreMap = new HashMap<>();
		
		List<IElectable> currentOrder = new ArrayList<>(origOrder);
		int currentScore = candidateList.size();
		
		for(IElectable c : currentBestCommittee) {
			scoreMap.put(c, currentScore);
			currentScore--;
			currentOrder.remove(c);
		}
		for(IElectable c : currentOrder) {
			scoreMap.put(c, currentScore);
			currentScore--;
		}
		
		
		
		System.out.println("************************* Agent : "+agent.getName()+" vote : ");
		for(IElectable c : candidateList){
			System.out.println(c.getName()+" vote : "+scoreMap.get(c));
		}
		return new AgentVote(agent, scoreMap);
	}
}
