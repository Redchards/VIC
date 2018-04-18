package org.upmc.electisim;

public abstract class ALearningAgentStrategy extends AStatefulAgentStrategy {

	public ALearningAgentStrategy(StateBuffer buffer, IVotingRule rule) {
		super(buffer, rule);
	}

}
