package org.upmc.electisim.knowledge;

import java.util.List;

import org.upmc.electisim.IElectable;

public interface IRankingKnowledgeDispenser extends IZeroKnowledgeDispenser {
	public List<IElectable> getLastCandidateRanking();
	public List<IElectable> getLastCommitteeRanking();
}
