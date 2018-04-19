package org.upmc.electisim;

import java.util.List;

public abstract class AStatefulAgentStrategy implements IAgentStrategy {
	protected StateBuffer buffer;
	protected IVotingRule rule;
	
	public AStatefulAgentStrategy(StateBuffer buffer, IVotingRule rule) {
		this.buffer = buffer;
		this.rule = rule;
	}
}
