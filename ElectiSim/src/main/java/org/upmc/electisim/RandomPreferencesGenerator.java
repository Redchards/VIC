package org.upmc.electisim;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class RandomPreferencesGenerator implements IPreferencesGenerator {

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
