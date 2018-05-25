package org.upmc.electisim;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A class representing a state of the simulation. It regroups the profile of the simulation,
 * all the votes of the agents and the final result of the election.
 */
public class SimulationState {

	/*
	 * (non-Javadoc)
	 * The profile of the simulation
	 */
	private SimulationProfile profile;
	
	/*
	 * (non-Javadoc)
	 * The list containing the results of the agent's votes
	 */
	private List<AgentVote> voteResults;
	
	/*
	 * (non-Javadoc)
	 * The result of the election
	 */
	private ElectionResult electionResult;
	
	/**
	 * Build a simulation state from a profile, a list of agents' votes and the result of the election.
	 * 
	 * @param profile The simulation profile
	 * @param voteResults The list of agents' votes
	 * @param electionResult The result of the election
	 */
	public SimulationState(SimulationProfile profile, List<AgentVote> voteResults, ElectionResult electionResult){
		this.profile = profile;
		this.voteResults = voteResults;
		this.electionResult = electionResult;
	}
	
	/**
	 * Build a simulation state by copying another simulation state. Copy constructor
	 * 
	 * @param other The simulation state to copy
	 */
	public SimulationState(SimulationState other) {
		this.profile = other.profile;
		this.voteResults = new ArrayList<>(other.voteResults);
	}

	/**
	 * @return The simulation profile
	 */
	public SimulationProfile getProfile(){
		return profile;
	}
	
	/**
	 * Get an agent's vote at the corresponding index
	 * 
	 * @param idx The index of the agent's vote to get
	 * @return The vote
	 */
	public AgentVote getVoteResult(int idx){
		return voteResults.get(idx);
	}
	
	/**
	 * @return An unmodifiable list containing the list of the agents' votes
	 */
	public List<AgentVote> getVoteResults() {
		return Collections.unmodifiableList(voteResults);
	}
	
	// TODO : Implement or remove
	/*public VoteResult fetchVoteResult(Agent agent) {
		int agentIdx = voteResults
	}*/
	
	/**
	 * @return The result of the election
	 */
	public ElectionResult getElectionResult() {
		return electionResult;
	}
	
}
