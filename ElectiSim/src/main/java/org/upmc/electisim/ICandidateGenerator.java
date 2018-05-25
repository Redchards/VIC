package org.upmc.electisim;

import java.util.List;

/**
 * An interface for the candidate generators. A candidate generator only needs to ensure that
 * the generated candidate is unique and not already present in the "candidateList" passed
 * in parameter.
 */
public interface ICandidateGenerator {
	/**
	 * Generates a candidate using the current candidate list. The method must ensure that
	 * the generated candidate is not already present in the candidate list
	 * 
	 * @param candidateList the current candidate list
	 * @return The generated candidate
	 */
	public Candidate generate(List<IElectable> candidateList);
}
