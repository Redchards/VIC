package org.upmc.electisim;

import java.util.List;

import org.upmc.electisim.knowledge.IStatefulKnowledgeDispenser;
import org.upmc.electisim.knowledge.OmniscientKnowledgeDispenser;

/**
 * A strategy class used to restrict the knowledge to a stateful knowledge of the situation.
 * The knowledge dispenser used will be {@link org.upmc.electisim.knowledge.StatefulKnowledgeDispenser}
 * and will provide the full state buffer.
 */
public abstract class AStatefulAgentStrategy implements IAgentStrategy {

	/*
	 * (non-Javadoc)
	 * Overrides the method and marks it final to disallow further overriding. Replaces it with {@link org.upmc.electisim.AStatefulAgentStrategy#executeVote(org.upmc.electisim.Agent, org.upmc.electisim.knowledge.StatefulKnowledgeDispenser, java.util.List, int)}
	 * @see org.upmc.electisim.IAgentStrategy#executeVote(org.upmc.electisim.Agent, org.upmc.electisim.knowledge.OmniscientKnowledgeDispenser, java.util.List, int)
	 */
	@Override
	public final AgentVote executeVote(Agent agent, OmniscientKnowledgeDispenser dispenser, List<IElectable> candidateList,
			int committeeSize) {
		return this.executeVote(agent, (IStatefulKnowledgeDispenser)dispenser, candidateList, committeeSize);
	}
	
	/**
	 * Executes the vote of the agent. 
	 * Designed to be overridden and aims to restrict the knowledge dispenser to a stateful knowledge dispenser 
	 * 
	 * @param agent The agent executing the vote
	 * @param dispenser The knowledge dispenser
	 * @param candidateList The list of candidates in the current vote
	 * @param committeeSize The size of the committee to vote for
	 * @return The result of the vote
	 */
	public abstract AgentVote executeVote(Agent agent, IStatefulKnowledgeDispenser dispenser, List<IElectable> candidateList,
			int committeeSize);
}
