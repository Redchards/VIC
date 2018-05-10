package org.upmc.electisim.knowledge;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.upmc.electisim.IElectable;
import org.upmc.electisim.ElectionResult;
import org.upmc.electisim.knowledge.IRankingKnowledgeDispenser;

public class RankingKnowledgeDispenser implements IRankingKnowledgeDispenser {
	
	private final ElectionResult electionResult;
	
	public RankingKnowledgeDispenser(ElectionResult electionResult) {
		this.electionResult = electionResult;
	}

	@Override
	public List<IElectable> getLastCandidateRanking() {
		return electionResult.generateAscendingCandidateRanking();
	}

	@Override
	public List<IElectable> getLastCommitteeRanking() {
		return this.getLastCandidateRanking().subList(0, electionResult.getElectedCommittee().size());
	}

}
