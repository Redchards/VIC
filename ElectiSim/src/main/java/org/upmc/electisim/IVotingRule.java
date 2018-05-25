
package org.upmc.electisim;

import java.util.List;

/**
 * The interface that every voting rule in the application must implement. At its core, a voting
 * rule will be inputted a list of agent vote and a  committee size and will produce a result
 * in the form of a {@link org.upmc.electisim.ElectionResult}. 
 */
public interface IVotingRule  {
	
	/**
	 * Generates an election result from a list of agent vote and a committee size
	 * 
	 * @param results the agent votes
	 * @param committeeSize the committee size
	 * @return The result of the election
	 */
	public ElectionResult getElectionResult(List<AgentVote> results, int committeeSize);
	
}
