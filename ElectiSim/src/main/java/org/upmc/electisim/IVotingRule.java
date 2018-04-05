
package org.upmc.electisim;

import java.util.List;

public interface IVotingRule {
	
	public List<Candidate> getElectedCommittee(List<VoteResult> results);
	
}
