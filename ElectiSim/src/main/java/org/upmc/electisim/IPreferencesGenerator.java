package org.upmc.electisim;

import java.util.List;

public interface IPreferencesGenerator {

	public Preferences generate(List<Candidate> candidateList, PreferenceType type, int desiredCommitteeSize);
}
