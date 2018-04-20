package org.upmc.electisim;

import java.util.List;

import org.upmc.electisim.knowledge.IStatefulKnowledgeDispenser;
import org.upmc.electisim.knowledge.OmniscientKnowledgeDispenser;

public abstract class AStatefulAgentStrategy implements IAgentStrategy {

	@Override
	public final VoteResult executeVote(Agent agent, OmniscientKnowledgeDispenser dispenser, List<Candidate> candidateList,
			int committeeSize) {
		return this.executeVote(agent, (IStatefulKnowledgeDispenser)dispenser, candidateList, committeeSize);
	}
	
	public abstract VoteResult executeVote(Agent agent, IStatefulKnowledgeDispenser dispenser, List<Candidate> candidateList,
			int committeeSize);
}