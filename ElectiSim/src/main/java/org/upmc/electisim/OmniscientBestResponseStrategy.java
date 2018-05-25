package org.upmc.electisim;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.upmc.electisim.knowledge.OmniscientKnowledgeDispenser;
import org.upmc.electisim.utils.CombinatoricsUtils;

/**
 * <p>A voting strategy with total knowledge in the class of best responses strategies, meaning
 * that this strategy doesn't use heuristics and instead will find the absolute best answer
 * to the voting problem assuming that the agents vote in isolation and hence to not communicate.
 * The decision is markovian, it only depends on the previous state and no other.</p>
 * <p>The strategy will simulate a vote for the given agent using every possible committee of
 * the requested size and will return the one which gave the best answer. This strategy
 * is thus highly dependant on the voting rule chosen.</p>
 */
public class OmniscientBestResponseStrategy implements IBestResponseAgentStrategy {

	/*
	 * (non-Javadoc)
	 * Executes the agent's vote
	 * @see org.upmc.electisim.IAgentStrategy#executeVote(org.upmc.electisim.Agent, org.upmc.electisim.knowledge.OmniscientKnowledgeDispenser, java.util.List, int)
	 */
	@Override
	public AgentVote executeVote(Agent agent, OmniscientKnowledgeDispenser dispenser, List<IElectable> candidateList, int committeeSize) {
		System.out.println(agent.getName());
		
		// If this is our first iteration, simply vote for our favourite committee.
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
		
		// Generate all the possible committees.
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
		
		// Generate a "blank result" with score at 0, useful to clean the current vote.
		AgentVote blankVoteResult = new AgentVote(results.get(agentIdx));
		for(IElectable c : candidateList) {
			blankVoteResult.setScore(c, 0);
		}
		
		List<IElectable> currentBestCommittee = state.getVoteResult(agentIdx).getLinearOrder();
		ElectionResult electionResult = dispenser.getVotingRule().getElectionResult(results, committeeSize);
		int bestDist = agent.getPreferences().getCommitteeDistance(electionResult.getElectedCommittee());
		List<IElectable> origOrder = results.get(agentIdx).getLinearOrder();
		
		// For all the possible committees, and all the possible permutations, we find the best.
		// In fact, we do not try all the permutations, but it's possible to prove that the ones
		// that we're trying here are sufficient.
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
		
		
		// Set the Borda scores
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
