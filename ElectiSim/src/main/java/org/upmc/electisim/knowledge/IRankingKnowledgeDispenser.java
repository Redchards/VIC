package org.upmc.electisim.knowledge;

import java.util.List;

import org.upmc.electisim.Candidate;

public interface IRankingKnowledgeDispenser extends IZeroKnowledgeDispenser {
	public List<Candidate> getLastCandidateRanking();
	public List<Candidate> getLastCommitteeRanking();
}
