package org.upmc.electisim.knowledge;

import java.util.List;

import org.upmc.electisim.Candidate;

public interface IRankingKnowledgeDispenser {
	public List<Candidate> getLastCandidateRanking();
	public List<Candidate> getLastCommitteeRanking();
}
