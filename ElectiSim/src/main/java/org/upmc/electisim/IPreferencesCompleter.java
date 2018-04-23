package org.upmc.electisim;

import java.io.Serializable;
import java.util.List;

public interface IPreferencesCompleter extends Serializable{
	public List<Candidate> completePreferences(Preferences pref, List<Candidate> preferenceList);
}
