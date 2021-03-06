package org.upmc.electisim;

import java.util.List;

import org.upmc.electisim.knowledge.IRankingKnowledgeDispenser;
import org.upmc.electisim.knowledge.OmniscientKnowledgeDispenser;

/**
 * A strategy class used to restrict the knowledge to a simple ranking of the candidates.
 * The knowledge dispenser used will be {@link org.upmc.electisim.knowledge.RankingKnowledgeDispenser}
 * and will only provide rankings from the previous state of the simulation.
 */
public abstract class ARankingAgentStrategy implements IAgentStrategy {

	/*
	 * (non-Javadoc)
	 * Override the method and marks it final to disallow further overriding. Replaces it with {@link org.upmc.electisim.ARankingAgentStrategy#executeVote(org.upmc.electisim.Agent, org.upmc.electisim.knowledge.RankingKnowledgeDispenser, java.util.List, int)}
	 * @see org.upmc.electisim.IAgentStrategy#executeVote(org.upmc.electisim.Agent, org.upmc.electisim.knowledge.OmniscientKnowledgeDispenser, java.util.List, int)
	 */
	@Override
	public final AgentVote executeVote(Agent agent, OmniscientKnowledgeDispenser dispenser, List<IElectable> candidateList,
			int committeeSize) {
		return this.executeVote(agent, (IRankingKnowledgeDispenser)dispenser, candidateList, committeeSize);
	}
	
	/**
	 * Execute the vote of the agent. 
	 * Designed to be overridden and aims to restrict the knowledge dispenser to a ranking knowledge dispenser 
	 * 
	 * @param agent the agent executing the vote
	 * @param dispenser the knowledge dispenser
	 * @param candidateList the list of candidates in the current vote
	 * @param committeeSize the size of the committee to vote for
	 * @return The result of the vote
	 */
	public abstract AgentVote executeVote(Agent agent, IRankingKnowledgeDispenser dispenser, List<IElectable> candidateList,
			int committeeSize);
}
