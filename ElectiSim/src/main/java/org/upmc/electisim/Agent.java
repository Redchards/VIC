package org.upmc.electisim;

public class Agent {
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
