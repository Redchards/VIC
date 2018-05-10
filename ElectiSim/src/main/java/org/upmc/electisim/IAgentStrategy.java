package org.upmc.electisim;


import java.util.List;

import org.upmc.electisim.knowledge.OmniscientKnowledgeDispenser;


public interface IAgentStrategy {
	public AgentVote executeVote(Agent agent, OmniscientKnowledgeDispenser dispenser, List<IElectable> candidateList, int committeeSize);
}
