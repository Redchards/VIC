package org.upmc.electisim;

import java.util.List;

public interface IAgentStrategy {
	public abstract VoteResult executeVote(Agent agent, List<Candidate> candidateList, int commiteeeSize);
}
