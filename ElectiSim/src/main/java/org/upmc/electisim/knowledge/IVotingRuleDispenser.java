package org.upmc.electisim.knowledge;

import org.upmc.electisim.IVotingRule;

public interface IVotingRuleDispenser extends IZeroKnowledgeDispenser {
	public IVotingRule getVotingRule();
}
