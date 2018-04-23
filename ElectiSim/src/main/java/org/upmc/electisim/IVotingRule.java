
package org.upmc.electisim;

import java.io.Serializable;
import java.util.List;

public interface IVotingRule extends Serializable {
	
	public ElectionResult getElectionResult(List<VoteResult> results, int committeeSize);
	
}
