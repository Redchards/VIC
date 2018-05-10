package org.upmc.electisim;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.upmc.electisim.utils.MapUtils;

public class AgentVote {
	private Agent agent;
	private Map<IElectable, Integer> scoreMap;
	
	public AgentVote(Agent agent) {
		this(agent, new HashMap<IElectable, Integer>());
	}
	
	
	public AgentVote(Agent agent, Map<IElectable, Integer> scoreMap) {
		this.agent = agent;
		this.scoreMap = new HashMap<>(scoreMap);
	}
	
	public AgentVote(AgentVote other) {
		this(other.agent, other.scoreMap);
	}
	
	
	public void setScore(IElectable candidate, int score){
		scoreMap.put(candidate, score);
	}

	
	public Agent getAgent(){
		return agent;
	}
	
	public Map<IElectable, Integer> getScoreMap(){
		return scoreMap;
	}
	
	public List<IElectable> getLinearOrder(){
		List<Entry<IElectable, Integer>> l = MapUtils.sortByValue(this.scoreMap);
		List<IElectable> res = new ArrayList<>();
		
		for(Entry<IElectable, Integer> e : l) {
			res.add(e.getKey());
		}
		
		return res;
	}
	
	public List<IElectable> getKBests(int k) {
		return this.getLinearOrder().subList(0, k);
	}
}
