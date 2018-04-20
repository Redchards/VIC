
package org.upmc.electisim;

import java.util.List;

public interface IVotingRule {
	
	public ElectionResult getElectionResult(List<VoteResult> results, int committeeSize);
	
}
