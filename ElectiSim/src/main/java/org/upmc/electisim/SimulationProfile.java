package org.upmc.electisim;

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
}
