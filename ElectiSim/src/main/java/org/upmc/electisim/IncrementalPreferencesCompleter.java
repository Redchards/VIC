package org.upmc.electisim;

import java.util.List;

public class IncrementalPreferencesCompleter implements IPreferencesCompleter {

	@Override
	public List<IElectable> completePreferences(Preferences pref, List<IElectable> preferenceList) {
		return preferenceList;
	}

}
