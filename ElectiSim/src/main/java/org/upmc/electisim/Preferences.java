package org.upmc.electisim;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Preferences {
	private SimulationProfile profile;
	private PreferenceType type;
	private List<Candidate> prefList;
	private IPreferencesCompleter prefCompleter;
	
	public Preferences(SimulationProfile profile, PreferenceType type) {
		this.Preferences(profile, type, new ArrayList<>());
	}
	
	public Preferences(SimulationProfile profile, PreferenceType type, List<Candidate> prefList) {
		this.Preferences(profile, type, prefList, new IncrementalPreferencesCompleter());
	}
	
	public Preferences(SimulationProfile profile, PreferenceType type, List<Candidate> prefList, IPreferencesCompleter completer) {
		this.profile = profile;
		this.type = type;
		this.prefList = prefList;
		this.prefCompleter = prefCompleter;
	}
	
	public PreferenceType getType() {
		return type;
	}
	
	public int getCandidateDistance(String candidateName) {
		int idx = -1;
		
		for(int i = 0; i < prefList.size(); i++) {
			if(prefList.get(i).getName().equals(candidateName)) {
				idx = i;
				break;
			}
		}
		
		return getCandidateDistance(idx);
	}
	
	public int getCandidateDistance(Candidate candidate) {
		if(candidate == null || prefList.indexOf(candidate) == -1) {
			return this.getCandidateDistance(-1);
		}
		return this.getCandidateDistance(prefList.indexOf(candidate));
	}
	
	public Optional<Integer> getCommitteeDistance(List<Candidate> committee) {
		return committee.stream().map(c -> this.getCandidateDistance(c)).reduce((a, b) -> a + b);
	}
	
	public void updateProfile(SimulationProfile profile) throws PreferencesInconsistencyException {
		this.profile = profile;
		
		this.checkPreferencesConsistency();
	}
	
	public void updatePreferences(List<Candidate> prefList) throws PreferencesInconsistencyException {
		List<Candidate> oldList = this.prefList;
		this.prefList = prefList;
		
		try {
			this.checkPreferencesConsistency();
		}
		catch(PreferencesInconsistencyException e) {
			this.prefList = oldList;
			throw e;
		}
	}
	
	protected int getCandidateDistance(int idx) {
		switch(this.type) {
		case HAMMING:
			return hammingBasedDistance(idx);
		case RESPONSIVE:
			return responsiveBasedDistance(idx);
		}
	}
	
	protected int hammingBasedDistance(int idx) {
		if(idx != -1) {
			return 0;
		}
		else {
			return 1;
		}		
	}
	
	protected int responsiveBasedDistance(int idx) {
		return idx != -1 ? idx : profile.getNumberOfCandidates();
	}
	
	protected void checkPreferencesConsistency() throws PreferencesInconsistencyException {
		List<Candidate> badCandidates = new ArrayList<>();
		
		for(Candidate c1 : prefList) {
			for(Candidate c2 : profile.getCandidateList) {
				if(!c1.equals(c2)) {
					badCandidates.add(c1);
				}
			}
		}
		
		if(badCandidates.size() > 0) {
			throw new PreferencesInconsistencyException(badCandidates);
		}
	}
}
