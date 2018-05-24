package org.upmc.electisim;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Preferences {
	
	private PreferenceType type;
	private List<IElectable> prefList;
	private IPreferencesCompleter prefCompleter;
	
	public Preferences(PreferenceType type) {
		this(type, new ArrayList<>()); 
	}
	
	public Preferences(PreferenceType type, List<IElectable> prefList) {
		this(type, prefList, new IncrementalPreferencesCompleter());
	}
	
	public Preferences(PreferenceType type, List<IElectable> prefList, IPreferencesCompleter completer) {
		this.type = type;
		this.prefList = prefList;
		this.prefCompleter = prefCompleter;
	}
	
	public PreferenceType getType() {
		return type;
	}
	
	public List<IElectable> getPreferenceList() {
		return prefList;
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
	
	public int getCandidateDistance(IElectable candidate) {
		if(candidate == null || prefList.indexOf(candidate) == -1) {
			return this.getCandidateDistance(-1);
		}
		// System.out.println("Index of " + candidate.getName() + " : " + prefList.indexOf(candidate));
		return this.getCandidateDistance(prefList.indexOf(candidate));
	}
	
	public int getCommitteeDistance(List<IElectable> committee) {
		int res = 0;
		
		for(IElectable c : committee) {
			res += getCandidateDistance(c);
		}
		
		return res;
		//return committee.stream().map(c -> this.getCandidateDistance(c)).reduce((a, b) -> a + b);
	}
	
	public void updatePreferences(List<IElectable> prefList) {
		List<IElectable> oldList = this.prefList;
		this.prefList = prefList;
	}
	
	// Note : at least one of the favourites
	public List<IElectable> favouriteCommittee(int committeeSize) {
		if(committeeSize < prefList.size()) {
			return prefList.subList(0, committeeSize);
		}
		return prefList;
	}
	
	protected int getCandidateDistance(int idx) {
		switch(this.type) {
		case HAMMING:
			return hammingBasedDistance(idx);
		case RESPONSIVE:
			return responsiveBasedDistance(idx);
		default : return -1;
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
		return idx != -1 ? idx : this.prefList.size();
	}
}
