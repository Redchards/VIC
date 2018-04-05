package org.upmc.electisim;

import java.util.HashMap;
import java.util.Map;

public class VoteResult {
	private SimulationProfile profile;
	private Agent agent;
	private Map<Candidate, Integer> scoreMap;
	
	public VoteResult(SimulationProfile profile, Agent agent) {
		this(profile, agent, new HashMap<Candidate, Integer>());
	}
	
	
	public VoteResult(SimulationProfile profile, Agent agent, Map<Candidate, Integer> scoreMap) {
		this.profile = profile;
		this.agent = agent;
		this.scoreMap = new HashMap<>(scoreMap);
	}
	
	
	public void setScore(Candidate candidate, int score){
		scoreMap.put(candidate, score);
	}
	
	public SimulationProfile getProfile(){
		return profile;
	}
	
	public Agent getAgent(){
		return agent;
	}
	
	public Map<Candidate, Integer> getScoreMap(){
		return scoreMap;
	}
}
