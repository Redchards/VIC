package org.upmc.electisim;


import java.util.List;

public interface IPreferencesCompleter {
	public List<IElectable> completePreferences(Preferences pref, List<IElectable> preferenceList);
}
