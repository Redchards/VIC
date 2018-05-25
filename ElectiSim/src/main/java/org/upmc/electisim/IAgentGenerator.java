package org.upmc.electisim;

import java.util.List;

/**
 * An interface for the different agent generators. An agent generator needs to ensure that
 * the agent name is unique and isn't already contained in the "agentList" parameter of the
 * {@link #generate(List, PreferenceType)} method. The agents can be generated with or without
 * preferences, this detail is left to the discretion of the programmer.
 */
public interface IAgentGenerator {
	/**
	 * Generates an agent from an agent list and a preference type. The method needs
	 * to ensure that the agent name is unique
	 * 
	 * @param agentList the list of current agents
	 * @param type the preferences type
	 * @return The generated agent
	 */
	public Agent generate(List<Agent> agentList, PreferenceType type);
}
