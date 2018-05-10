package org.upmc.electisim;

import java.util.List;

import org.upmc.electisim.knowledge.IRankingKnowledgeDispenser;
import org.upmc.electisim.knowledge.OmniscientKnowledgeDispenser;

public abstract class ARankingAgentStrategy implements IAgentStrategy {

	@Override
	public final AgentVote executeVote(Agent agent, OmniscientKnowledgeDispenser dispenser, List<IElectable> candidateList,
			int committeeSize) {
		return this.executeVote(agent, (IRankingKnowledgeDispenser)dispenser, candidateList, committeeSize);
	}
	
	public abstract AgentVote executeVote(Agent agent, IRankingKnowledgeDispenser dispenser, List<IElectable> candidateList,
			int committeeSize);
}
