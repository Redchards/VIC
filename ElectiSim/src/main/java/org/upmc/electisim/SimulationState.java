package org.upmc.electisim;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class SimulationState {

	private SimulationProfile profile;
	private List<AgentVote> voteResults;
	private ElectionResult electionResult;
	
	public SimulationState(SimulationProfile profile, List<AgentVote> voteResults, ElectionResult electionResult){
		this.profile = profile;
		this.voteResults = voteResults;
		this.electionResult = electionResult;
	}
	
	public SimulationState(SimulationState other) {
		this.profile = other.profile;
		this.voteResults = new ArrayList<>(other.voteResults);
	}

	public SimulationProfile getProfile(){
		return profile;
	}
	
	public AgentVote getVoteResult(int idx){
		return voteResults.get(idx);
	}
	
	public List<AgentVote> getVoteResults() {
		return Collections.unmodifiableList(voteResults);
	}
	
	// TODO : Implement or remove
	/*public VoteResult fetchVoteResult(Agent agent) {
		int agentIdx = voteResults
	}*/
	
	public ElectionResult getElectionResult() {
		return electionResult;
	}
	
}
