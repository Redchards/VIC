package org.upmc.electisim;


import java.util.List;

public interface IPreferencesCompleter {
	public List<Candidate> completePreferences(Preferences pref, List<Candidate> preferenceList);
}
