package org.upmc.electisim;

import java.util.List;

public abstract class AStatefulAgentStrategy {
	private StateBuffer buffer;
	private IVotingRule rule;
	
	public AStatefulAgentStrategy(StateBuffer buffer, IVotingRule rule) {
		this.buffer = buffer;
		this.rule = rule;
	}
	
	public abstract VoteResult executeVote(Agent agent, List<Candidate> candidateList);
}
