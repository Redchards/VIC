package org.upmc.electisim;

public abstract class ABetterResponseAgentStrategy extends AStatefulAgentStrategy {

	public ABetterResponseAgentStrategy(StateBuffer buffer, IVotingRule rule) {
		super(buffer, rule);
	}

}
