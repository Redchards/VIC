package org.upmc.electisim;

/**
 * A class representing an agent, also referred to as a voter in the application.
 * This class essentially acts as a token for the voting process and should not get much
 * more complex in future versions.
 **/
public class Agent {
	
	/*
	 * (non-Javadoc)
	 * The name of the agent
	 */
	private String name;
	
	/*
	 * The preferences of the agent
	 */
	private Preferences pref;
	
	/**
	 * Builds an agent from a name and preferences
	 * 
	 * @param name The name of the agent
	 * @param pref The preferences of the agent
	 * @see Preferences
	 */
	public Agent(String name, Preferences pref) {
		this.name = name;
		this.pref = pref;
	}
	
	/**
	 * @return the name of the agent
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * @return the preferences of the agent
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
