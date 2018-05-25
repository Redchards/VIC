package org.upmc.electisim;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * A random preferences generator which will used a simple shuffle on the candidate list
 * to generate the preferences.
 */
public class RandomPreferencesGenerator implements IPreferencesGenerator {

	/*
	 * (non-Javadoc)
	 * Generates the preferences
	 * 
	 * @see org.upmc.electisim.IPreferencesGenerator#generate(java.util.List, org.upmc.electisim.PreferenceType, int)
	 */
	@Override
	public Preferences generate(List<IElectable> candidateList, PreferenceType type, int desiredCommitteeSize) {
	    List<IElectable> shuffledList = new LinkedList<>(candidateList);
	    Collections.shuffle(shuffledList);
		
		switch(type) {
		case HAMMING:
		    return new Preferences(type, shuffledList.subList(0, desiredCommitteeSize));
		case RESPONSIVE:
			return new Preferences(type, shuffledList);
		default:
			return null;
		}
			
	}

}
