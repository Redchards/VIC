package org.upmc.electisim;

import java.io.Serializable;

public class Agent implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8545937434570879358L;
	private String name;
	private Preferences pref;
	
	public Agent(String name, Preferences pref) {
		this.name = name;
		this.pref = pref;
	}
	
	public String getName() {
		return name;
	}
	
	public Preferences getPreferences() {
		return pref;
	}
	
	@Override
	public String toString() {
		return getName();
	}
}
