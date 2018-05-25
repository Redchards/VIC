package org.upmc.electisim;


import java.util.ArrayList;
import java.util.List;

/**
 * The preferences of an agent in the simulation. The preferences can be of different types
 * contained in {@link org.upmc.electisim.PreferenceType} and are generally represented
 * as a list of electable entities. The preferences can also be completed using a preference
 * completer (TODO : rather unused for now, the API need amendements).
 */
public class Preferences {
	
	/*
	 * (non-Javadoc)
	 * The preference type of the current preferences
	 */
	private PreferenceType type;
	
	/*
	 * (non-Javadoc)
	 * The list representing the preferences
	 */
	private List<IElectable> prefList;
	
	/*
	 * (non-javaodoc)
	 * The preference completer
	 */
	private IPreferencesCompleter prefCompleter;
	
	/**
	 * Build preferences from a type. The preference list will thus be empty and the preference
	 * completer will be the default one {@link org.upmc.electisim.IncrementalPreferencesCompleter}
	 * 
	 * @param type the preference type
	 */
	public Preferences(PreferenceType type) {
		this(type, new ArrayList<>()); 
	}
	
	/**
	 * Build preferences from a type and a list of preferences. The preference completer
	 * will be the default one completer will be the default one {@link org.upmc.electisim.IncrementalPreferencesCompleter}
	 * 
	 * @param type the preference type
	 * @param prefList the list to use to represent the preferences
	 */
	public Preferences(PreferenceType type, List<IElectable> prefList) {
		this(type, new ArrayList<>(prefList), new IncrementalPreferencesCompleter());
	}
	
	/**
	 * Build the preferences from a type, a list and a preference completer
	 * 
	 * @param type the preference type
	 * @param prefList the list to use to represent the preferences
	 * @param completer the preference completer
	 */
	public Preferences(PreferenceType type, List<IElectable> prefList, IPreferencesCompleter completer) {
		this.type = type;
		this.prefList = prefList;
		this.prefCompleter = prefCompleter;
	}
	
	/**
	 * @return The preference type
	 */
	public PreferenceType getType() {
		return type;
	}
	
	/**
	 * @return The preference list
	 */
	public List<IElectable> getPreferenceList() {
		return prefList;
	}
	
	/**
	 * @param candidateName the name of the candidate we want to get the distance of
	 * @return The distance of the given candidate
	 */
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
	
	/**
	 * @param candidate the candidate we want to get the distance of
	 * @return The distance of the given candidate
	 */
	public int getCandidateDistance(IElectable candidate) {
		if(candidate == null || prefList.indexOf(candidate) == -1) {
			return this.getCandidateDistance(-1);
		}
		return this.getCandidateDistance(prefList.indexOf(candidate));
	}
	
	/**
	 * @param committee the committee we want to get the distance of
	 * @return the distance of the given committee
	 */
	public int getCommitteeDistance(List<IElectable> committee) {
		int res = 0;
		
		for(IElectable c : committee) {
			res += getCandidateDistance(c);
		}
		
		return res;
	}
	
	/**
	 * Update the preferences using a new preferences list
	 * 
	 * @param prefList the new preferences list
	 */
	public void updatePreferences(List<IElectable> prefList) {		
		this.prefList = new ArrayList<>(prefList);
	}
	
	/**
	 * Will return one of the favourite committee considering the preferences
	 * 
	 * @param committeeSize the size of the desired committee
	 * @return One of the best committees
	 */
	public List<IElectable> favouriteCommittee(int committeeSize) {
		if(committeeSize < prefList.size()) {
			return prefList.subList(0, committeeSize);
		}
		return prefList;
	}
	
	/**
	 * Get the candidate distance based on its index in the preferences list
	 * 
	 * @param idx the index of the candidate
	 * @return The distance of the given candidate
	 */
	protected int getCandidateDistance(int idx) {
		switch(this.type) {
		case HAMMING:
			return hammingBasedDistance(idx);
		case RESPONSIVE:
			return responsiveBasedDistance(idx);
		default : return -1;
		}
	}
	
	/**
	 * Compute the Hamming distance of the candidate based on its index in the preferences 
	 * list
	 * 
	 * @param idx the index of the candidate
	 * @return The hamming distance of the candidate
	 */
	protected int hammingBasedDistance(int idx) {
		if(idx != -1) {
			return 0;
		}
		else {
			return 1;
		}		
	}
	
	/**
	 * Compute the responsive distance of the candidate based on its index in the preferences 
	 * list
	 * 
	 * @param idx the index of the candidate
	 * @return The responsive distance of the candidate
	 */
	protected int responsiveBasedDistance(int idx) {
		return idx != -1 ? idx : this.prefList.size();
	}
}
