package org.upmc.electisim.knowledge;

import java.util.List;

import org.upmc.electisim.IElectable;

/**
 * An interface describing the methods of a ranking knowledge dispenser. Such dispenser will
 * only provide informations about the ranking of the candidates from the last iteration.
 */
public interface IRankingKnowledgeDispenser extends IZeroKnowledgeDispenser {
	/**
	 * @return The ranking of all the candidates from the last iteration
	 */
	public List<IElectable> getLastCandidateRanking();
	
	/**
	 * @return The ranking of the elected committee from the last iteration
	 */
	public List<IElectable> getLastCommitteeRanking();
}
