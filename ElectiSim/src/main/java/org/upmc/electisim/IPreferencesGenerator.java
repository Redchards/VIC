package org.upmc.electisim;

import java.util.List;

/**
 * An interface for the preferences generator. The generator must ensure that the returned
 * preferences are well formed
 */
public interface IPreferencesGenerator {

	/**
	 * Generate the preferences for one agent
	 * 
	 * @param candidateList the list of candidates to pick from
	 * @param type the preference type
	 * @param desiredCommitteeSize the desired committee size
	 * @return The generated preferences
	 */
	public Preferences generate(List<IElectable> candidateList, PreferenceType type, int desiredCommitteeSize);
}
