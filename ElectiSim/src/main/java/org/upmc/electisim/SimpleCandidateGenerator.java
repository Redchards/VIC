package org.upmc.electisim;

import java.util.List;

/**
 * A simple candidate generator, incrementing a simple counter to generate unique names for the 
 * candidates. The naming convention used is "Ci" where "i" is a number.
 */
public class SimpleCandidateGenerator implements ICandidateGenerator {

	/*
	 * (non-Javadoc)
	 * The candidate based name
	 */
	private static final String DEFAULT_CANDIDATE_BASE_NAME = "C";
	
	/*
	 * (non-Javadoc)
	 * Generate a candidate
	 * 
	 * @see org.upmc.electisim.ICandidateGenerator#generate(java.util.List)
	 */
	@Override
	public Candidate generate(List<IElectable> candidateList) {
		int candidateNumber = 1;
		String currentCandidateName = null;
		boolean needsRecheck = true;
		System.out.println(candidateList);
		while(needsRecheck) {
			needsRecheck = false;
			currentCandidateName = (DEFAULT_CANDIDATE_BASE_NAME + Integer.toString(candidateNumber));
			for(IElectable c : candidateList) {
				if(currentCandidateName.equals(c.getName())) {
					candidateNumber++;
					needsRecheck = true;
					break;
				}
			}
		}
		
		return new Candidate(currentCandidateName);
	}

}
