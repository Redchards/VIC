package org.upmc.electisim;

import java.util.List;

public class SimulationState {

	private SimulationProfile profile;
	private List<VoteResult> voteResults;
	
	public SimulationState(SimulationProfile profile, List<VoteResult> voteResults){
		this.profile = profile;
		this.voteResults = voteResults;
	}

	public SimulationProfile getProfile(){
		return profile;
	}
	
	public List<VoteResult> getVoteResults(){
		return voteResults;
	}
	
}
