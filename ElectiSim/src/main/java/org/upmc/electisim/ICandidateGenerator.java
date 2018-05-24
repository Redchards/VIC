package org.upmc.electisim;

import java.util.List;

public interface ICandidateGenerator {
	public Candidate generate(List<IElectable> candidateList);
}
