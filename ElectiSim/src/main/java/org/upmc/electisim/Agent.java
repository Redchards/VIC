package org.upmc.electisim;

import java.io.Serializable;

/**
 * A class representing an agent, also referred to as a voter in the application.
 * This class essentially acts as a token for the voting process and should not get much
 * more complex in future versions.
 **/
public class Agent implements Serializable {
	
	/*
	 * (non-Javadoc)
	 * Generated serial UID 
	 */
	private static final long serialVersionUID = -8545937434570879358L;

	/*
	 * (non-Javadoc)
	 * The name of the agent
	 */
	private String name;
	
	/*
	 * (non-Javadoc)
	 * The preferences of the agent
	 */
	private Preferences pref;
	
	/**
	 * Build an agent from a name and preferences
	 * 
	 * @param name the name of the agent
	 * @param pref the preferences of the agent
	 * @see Preferences
	 */
	public Agent(String name, Preferences pref) {
		this.name = name;
		this.pref = pref;
	}
	
	/**
	 * @return The name of the agent
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * @return The preferences of the agent
	 */
	public Preferences getPreferences() {
		return pref;
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return getName() + " : " + this.pref.getPreferenceList();
	}
}
