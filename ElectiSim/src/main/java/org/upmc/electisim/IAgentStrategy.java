package org.upmc.electisim;


import java.util.List;

import org.upmc.electisim.knowledge.OmniscientKnowledgeDispenser;

/**
 * An interface for the agent strategies. In its most basic form, a strategy will need only one
 * method, the {@link #executeVote(Agent, OmniscientKnowledgeDispenser, List, int)} method, and
 * will be provided with a {@link org.upmc.electisim.knowledge.OmniscientKnowledgeDispenser}, but
 * restrictions on the knowledge can be made using classes such as {@link org.upmc.electisim.ARankingAgentStrategy}
 * or {@link org.upmc.electisim.AStatefulAgentStrategy}.
 */
public interface IAgentStrategy {
	/**
	 * Execute the vote of the agent and returns the vote
	 * 
	 * @param agent the agent emitting the vote
	 * @param dispenser the knowledge dispenser to use of this voting process
	 * @param candidateList the list of candidates
	 * @param committeeSize the size of the desired committee
	 * @return
	 */
	public AgentVote executeVote(Agent agent, OmniscientKnowledgeDispenser dispenser, List<IElectable> candidateList, int committeeSize);
}
