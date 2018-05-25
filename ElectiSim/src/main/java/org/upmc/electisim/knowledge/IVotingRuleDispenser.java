package org.upmc.electisim.knowledge;

import org.upmc.electisim.IVotingRule;

/**
 * An interface describing the methods provided by a voting rule dispenser. Such dispenser will
 * only provide knowledge about the voting rule.
 */
public interface IVotingRuleDispenser extends IZeroKnowledgeDispenser {
	/**
	 * @return The voting rule used by the simulation
	 */
	public IVotingRule getVotingRule();
}
