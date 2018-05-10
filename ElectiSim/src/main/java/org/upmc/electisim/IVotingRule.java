
package org.upmc.electisim;

import java.util.List;

public interface IVotingRule  {
	
	public ElectionResult getElectionResult(List<AgentVote> results, int committeeSize);
	
}
