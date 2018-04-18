package org.upmc.electisim;

import java.util.HashMap;
import java.util.Map;

public class VoteResult {
	private Agent agent;
	private Map<Candidate, Integer> scoreMap;
	
	public VoteResult(Agent agent) {
		this(agent, new HashMap<Candidate, Integer>());
	}
	
	
	public VoteResult(Agent agent, Map<Candidate, Integer> scoreMap) {
		this.agent = agent;
		this.scoreMap = new HashMap<>(scoreMap);
	}
	
	
	public void setScore(Candidate candidate, int score){
		scoreMap.put(candidate, score);
	}

	
	public Agent getAgent(){
		return agent;
	}
	
	public Map<Candidate, Integer> getScoreMap(){
		return scoreMap;
	}
}
