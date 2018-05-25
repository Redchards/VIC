package org.upmc.electisim;


import java.util.Collections;
import java.util.List;

/**
 * A class used to aggregate the necessary information about the simulation, thus creating a 
 * profile. It regroups the preferences type, the voting rule, the agent strategy, the agent list,
 * the candidate list and finally the desired committee size. This class has no real functionality.
 * This is an immutable type.
 */
public class SimulationProfile {
	
	/*
	 * (non-Javadoc)
	 * The type of the preferences
	 */
	private final PreferenceType type;
	
	/*
	 * (non-Javadoc)
	 * The voting rule
	 */
	private final IVotingRule rule;
	
	/*
	 * (non-Javadoc)
	 * The agent strategy
	 */
	private final IAgentStrategy strategy;
	
	/*
	 * (non-Javadoc)
	 * The agent list
	 */
	private final List<Agent> agentList;
	
	/*
	 * (non-Javadoc)
	 * The candidate list
	 */
	private final List<IElectable> candidateList;
	
	/*
	 * (non-Javadoc)
	 * The desired size of the committee
	 */
	private final int committeeSize;
	
	public SimulationProfile(PreferenceType type, IVotingRule rule, IAgentStrategy strategy, List<Agent> agentList, List<IElectable> candidateList, int committeeSize) {
		this.type = type;
		this.rule = rule;
		this.strategy = strategy;
		this.agentList = agentList;
		this.candidateList = candidateList;
		this.committeeSize = committeeSize;
	}
	
	/**
	 * @return The voting rule
	 */
	public IVotingRule getVotingRule() {
		return rule;
	}
	
	/**
	 * @return The voting strategy
	 */
	public IAgentStrategy getVotingStrategy() {
		return strategy;
	}
	
	/**
	 * @return The agent list
	 */
	public List<Agent> getAgentList() {
		return Collections.unmodifiableList(agentList);
	}
	
	/**
	 * @return The candidate list
	 */
	public List<IElectable> getCandidateList() {
		return Collections.unmodifiableList(candidateList);
	}
	
	/**
	 * @return The preferences type
	 */
	public PreferenceType getPreferenceType() {
		return type;
	}
	
	/**
	 * @return The number of candidates
	 */
	public int getNumberOfCandidates() {
		return candidateList.size();
	}
	
	/**
	 * @return The desired size of the committee
	 */
	public int getCommitteeSize() {
		return committeeSize;
	}
}
