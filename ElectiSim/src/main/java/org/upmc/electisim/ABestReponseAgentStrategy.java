package org.upmc.electisim;

public abstract class ABestReponseAgentStrategy extends AStatefulAgentStrategy {

	public ABestReponseAgentStrategy(StateBuffer buffer, IVotingRule rule) {
		super(buffer, rule);
	}

}
