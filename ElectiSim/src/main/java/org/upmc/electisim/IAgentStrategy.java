package org.upmc.electisim;

import java.util.List;

public interface IAgentStrategy {
	public List<Candidate> getElectedCommittee(List<VoteResult> results);
}
