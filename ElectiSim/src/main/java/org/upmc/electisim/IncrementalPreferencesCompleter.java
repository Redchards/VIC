package org.upmc.electisim;

import java.util.List;

public class IncrementalPreferencesCompleter implements IPreferencesCompleter {

	@Override
	public List<Candidate> completePreferences(Preferences pref, List<Candidate> preferenceList) {
		return preferenceList;
	}

}
