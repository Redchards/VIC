package org.upmc.electisim.knowledge;

import org.upmc.electisim.IVotingRule;

/**
 * An implementation of the voting rule knowledge dispenser
 * 
 * @see org.upmc.electisim.knowledge.IVotingRuleDispenser
 */
public class VotingRuleDispenser implements IVotingRuleDispenser {
	
	/*
	 * (non-Javadoc)
	 * The voting rule used by the simulation
	 */
	private IVotingRule rule;
	
	/**
	 * Build a voting rule dispenser from the voting rule used by the simulation
	 * 
	 * @param rule the voting rule used by the simulation
	 */
	public VotingRuleDispenser(IVotingRule rule) {
		this.rule = rule;
	}

	/*
	 * (non-Javadoc)
	 * Get the voting rule used by the simulation
	 * 
	 * @see org.upmc.electisim.knowledge.IVotingRuleDispenser#getVotingRule()
	 */
	@Override
	public IVotingRule getVotingRule() {
		return rule;
	}

}
