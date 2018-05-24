package org.upmc.electisim;


import java.util.Collections;
import java.util.List;

public class SimulationProfile {
	
	private final PreferenceType type;
	private final IVotingRule rule;
	private final IAgentStrategy strategy;
	private final List<Agent> agentList;
	private final List<IElectable> candidateList;
	private final int committeeSize;
	
	public SimulationProfile(PreferenceType type, IVotingRule rule, IAgentStrategy strategy, List<Agent> agentList, List<IElectable> candidateList, int committeeSize) {
		this.type = type;
		this.rule = rule;
		this.strategy = strategy;
		this.agentList = agentList;
		this.candidateList = candidateList;
		this.committeeSize = committeeSize;
	}
	
	
	public IVotingRule getVotingRule() {
		return rule;
	}
	
	public IAgentStrategy getVotingStrategy() {
		return strategy;
	}
	
	public List<Agent> getAgentList() {
		return Collections.unmodifiableList(agentList);
	}
	
	public List<IElectable> getCandidateList() {
		return Collections.unmodifiableList(candidateList);
	}
	
	public PreferenceType getPreferenceType() {
		return type;
	}
	
	public int getNumberOfCandidates() {
		return candidateList.size();
	}
	
	public int getCommitteeSize() {
		return committeeSize;
	}
}
