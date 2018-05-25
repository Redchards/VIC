package org.upmc.electisim;

import java.util.List;

/**
 * Incrementally complete the preferences of an agent
 * WARNING : Defunkt. Should be implemented later.
 */
public class IncrementalPreferencesCompleter implements IPreferencesCompleter {

	@Override
	public List<IElectable> completePreferences(Preferences pref, List<IElectable> preferenceList) {
		return preferenceList;
	}

}
