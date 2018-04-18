package org.upmc.electisim;

import java.util.List;

public class SimulationProfile {
	private PreferenceType type;
	private IVotingRule rule;
	private IAgentStrategy strategy;
	private List<Agent> agentList;
	private List<Candidate> candidateList;
	
	public SimulationProfile(PreferenceType type, IVotingRule rule, IAgentStrategy strategy) {
		this.type = type;
		this.rule = rule;
		this.strategy = strategy;
	}
	
	
	public IVotingRule getVotingRule(){
		return rule;
	}
	
	public IAgentStrategy getVotingStrategy(){
		return strategy;
	}
	
	public List<Agent> getAgentList(){
		return agentList;
	}
	
	public List<Candidate> getCandidateList(){
		return candidateList;
	}
	
	public PreferenceType getPreferenceType(){
		return type;
	}
	
	public int getNumberOfCandidates(){
		return candidateList.size();
	}
}
