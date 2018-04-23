package org.upmc.electisim;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

public class SimulationProfile implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -898187397432623125L;
	
	private PreferenceType type;
	private IVotingRule rule;
	private IAgentStrategy strategy;
	private final List<Agent> agentList;
	private final List<Candidate> candidateList;
	
	public SimulationProfile(PreferenceType type, IVotingRule rule, IAgentStrategy strategy, List<Agent> agentList, List<Candidate> candidateList) {
		this.type = type;
		this.rule = rule;
		this.strategy = strategy;
		this.agentList = agentList;
		this.candidateList = candidateList;
	}
	
	
	public IVotingRule getVotingRule(){
		return rule;
	}
	
	public IAgentStrategy getVotingStrategy(){
		return strategy;
	}
	
	public List<Agent> getAgentList(){
		return Collections.unmodifiableList(agentList);
	}
	
	public List<Candidate> getCandidateList(){
		return Collections.unmodifiableList(candidateList);
	}
	
	public PreferenceType getPreferenceType(){
		return type;
	}
	
	public int getNumberOfCandidates(){
		return candidateList.size();
	}
}
