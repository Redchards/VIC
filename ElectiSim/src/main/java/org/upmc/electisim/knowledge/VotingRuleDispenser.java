package org.upmc.electisim.knowledge;

import org.upmc.electisim.IVotingRule;

public class VotingRuleDispenser implements IVotingRuleDispenser {
	
	private IVotingRule rule;
	
	public VotingRuleDispenser(IVotingRule rule) {
		this.rule = rule;
	}

	@Override
	public IVotingRule getVotingRule() {
		return rule;
	}

}
