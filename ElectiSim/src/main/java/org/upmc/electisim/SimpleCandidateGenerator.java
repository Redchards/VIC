package org.upmc.electisim;

import java.util.List;

public class SimpleCandidateGenerator implements ICandidateGenerator {

	private static final String DEFAULT_CANDIDATE_BASE_NAME = "C";
	
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
