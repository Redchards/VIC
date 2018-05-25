package org.upmc.electisim;

import java.util.ArrayList;
import java.util.List;

/**
 * A class used to validate the preferences of a candidate and check if they're consistant
 * with the current simulation configuration. If not, an exception will be thrown and action
 * but be taken to ensure that the simulation doesn't start with erroneous informations.
 */
public class PreferencesValidator {
	/*
	 * (non-javadoc)
	 * The profile of the simulation
	 * 
	 */
	private SimulationProfile profile;
	
	/**
	 * Builds a validator from a simulation profile
	 * 
	 * @param profile the profile of the simulation
	 */
	public PreferencesValidator(SimulationProfile profile) {
		this.profile = profile;
	}
	
	/**
	 * @return The profile of the simulation used to perform the checks
	 */
	public SimulationProfile getSimulationProfile() {
		return profile;
	}
	
	/**
	 * Checks the consistency of the preferences and throws if inconsistencies are found with
	 * details information about them
	 * 
	 * @throws PreferencesInconsistencyException
	 */
	public void checkPreferencesConsistency() throws PreferencesInconsistencyException {
		List<IElectable> badCandidates = new ArrayList<>();
		
		for(Agent agent : profile.getAgentList()) {
			for(IElectable c1 : agent.getPreferences().getPreferenceList()) {
				for(IElectable c2 : profile.getCandidateList()) {
					if(!c1.equals(c2)) {
						badCandidates.add(c1);
					}
				}
			}
			
			if(!badCandidates.isEmpty()) {
				throw new PreferencesInconsistencyException(badCandidates, agent);
			}
		}
	}
}
