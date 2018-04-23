package org.upmc.electisim;

import java.io.Serializable;
import java.util.List;

import org.upmc.electisim.knowledge.OmniscientKnowledgeDispenser;


public interface IAgentStrategy extends Serializable {
	public VoteResult executeVote(Agent agent, OmniscientKnowledgeDispenser dispenser, List<Candidate> candidateList, int committeeSize);
}
