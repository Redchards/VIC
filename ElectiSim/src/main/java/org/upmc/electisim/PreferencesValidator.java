package org.upmc.electisim;

import java.util.ArrayList;
import java.util.List;

public class PreferencesValidator {
	private SimulationProfile profile;
	
	public PreferencesValidator(SimulationProfile profile) {
		this.profile = profile;
	}
	
	public SimulationProfile getSimulationProfile() {
		return profile;
	}
	
	public void checkPreferencesConsistency() throws PreferencesInconsistencyException {
		List<Candidate> badCandidates = new ArrayList<>();
		
		for(Agent agent : profile.getAgentList()) {
			for(Candidate c1 : agent.getPreferences().getPreferenceList()) {
				for(Candidate c2 : profile.getCandidateList()) {
					if(!c1.equals(c2)) {
						badCandidates.add(c1);
					}
				}
			}
			
			if(badCandidates.size() > 0) {
				throw new PreferencesInconsistencyException(badCandidates, agent);
			}
		}
	}
}
