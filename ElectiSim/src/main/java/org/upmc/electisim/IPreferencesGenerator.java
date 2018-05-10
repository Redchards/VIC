package org.upmc.electisim;

import java.util.List;

public interface IPreferencesGenerator {

	public Preferences generate(List<IElectable> candidateList, PreferenceType type, int desiredCommitteeSize);
}
