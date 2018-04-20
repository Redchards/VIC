package org.upmc.electisim;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class SimulationState {

	private SimulationProfile profile;
	private List<VoteResult> voteResults;
	private ElectionResult electionResult;
	
	public SimulationState(SimulationProfile profile, List<VoteResult> voteResults, ElectionResult electionResult){
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
	
	public VoteResult getVoteResult(int idx){
		return voteResults.get(idx);
	}
	
	public List<VoteResult> getVoteResults() {
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
