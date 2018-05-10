package org.upmc.electisim;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.upmc.electisim.utils.MapUtils;

public class AgentVote {
	private Agent agent;
	private Map<Candidate, Integer> scoreMap;
	
	public AgentVote(Agent agent) {
		this(agent, new HashMap<Candidate, Integer>());
	}
	
	
	public AgentVote(Agent agent, Map<Candidate, Integer> scoreMap) {
		this.agent = agent;
		this.scoreMap = new HashMap<>(scoreMap);
	}
	
	public AgentVote(AgentVote other) {
		this(other.agent, other.scoreMap);
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
	
	public List<Candidate> getLinearOrder(){
		List<Entry<Candidate, Integer>> l = MapUtils.sortByValue(this.scoreMap);
		List<Candidate> res = new ArrayList<>();
		
		for(Entry<Candidate, Integer> e : l) {
			res.add(e.getKey());
		}
		
		return res;
	}
	
	public List<Candidate> getKBests(int k) {
		return this.getLinearOrder().subList(0, k);
	}
}
