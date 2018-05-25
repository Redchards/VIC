package org.upmc.electisim.knowledge;

import org.upmc.electisim.SimulationState;

/**
 * An interface describing the methods of a stateful knowledge dispenser. Such dispenser is quite
 * powerful as it can provide information about everything in the state buffer of the simulation.
 */
public interface IStatefulKnowledgeDispenser extends IZeroKnowledgeDispenser {
	/**
	 * @return The last simulation state
	 */
	public SimulationState getLastSimulationState();
	
	/**
	 * Retrieve a simulation state at a specific index
	 * 
	 * @param idx the index of the requested state
	 * @return The requested state, if it exists
	 */
	public SimulationState getSimulationState(int idx);
	
	/**
	 * @return true if the current iteration is the first, false otherwise
	 */
	public boolean firstIteration();
}
