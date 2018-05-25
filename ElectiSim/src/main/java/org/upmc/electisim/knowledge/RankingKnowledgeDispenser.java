package org.upmc.electisim.knowledge;

import java.util.List;

import org.upmc.electisim.IElectable;
import org.upmc.electisim.ElectionResult;
import org.upmc.electisim.knowledge.IRankingKnowledgeDispenser;

/**
 * An implementation of the ranking knowledge dispenser
 * 
 * @see org.upmc.electisim.IRankingKnowledgeDispenser
 */
public class RankingKnowledgeDispenser implements IRankingKnowledgeDispenser {
	
	/*
	 * (non-Javadoc)
	 * The election result of the last iteration
	 */
	private final ElectionResult electionResult;
	
	/**
	 * Build a knowledge dispenser from the last election result
	 * 
	 * @param electionResult the election result, assumed to be the most current one
	 */
	public RankingKnowledgeDispenser(ElectionResult electionResult) {
		this.electionResult = electionResult;
	}

	/*
	 * (non-Javadoc)
	 * Get the ranking of all the candidates from the last state
	 * 
	 * @see org.upmc.electisim.knowledge.IRankingKnowledgeDispenser#getLastCandidateRanking()
	 */
	@Override
	public List<IElectable> getLastCandidateRanking() {
		return electionResult.generateAscendingCandidateRanking();
	}
	
	/*
	 * (non-Javadoc)
	 * Get the ranking of the elected committee from the last state
	 * 
	 * @see org.upmc.electisim.knowledge.IRankingKnowledgeDispenser#getLastCandidateRanking()
	 */
	@Override
	public List<IElectable> getLastCommitteeRanking() {
		return this.getLastCandidateRanking().subList(0, electionResult.getElectedCommittee().size());
	}

}
