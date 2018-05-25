package org.upmc.electisim.knowledge;

import org.upmc.electisim.SimulationState;
import org.upmc.electisim.StateBuffer;

/**
 * An implementation of the stateful knowledge dispenser
 * 
 * @see org.upmc.electisim.knowledge.IStatefulKnowledgeDispenser
 */
public class StatefulKnowledgeDispenser implements IStatefulKnowledgeDispenser {
	
	/*
	 * (non-Javadoc)
	 * The current state buffer
	 */
	private StateBuffer stateBuffer;
	
	/**
	 * Build a stateful knowledge dispenser using a statebuffer
	 * 
	 * @param stateBuffer the current state buffer
	 */
	public StatefulKnowledgeDispenser(StateBuffer stateBuffer) {
		this.stateBuffer = stateBuffer;
	}

	/*
	 * (non-Javadoc)
	 * Get the last simulation state
	 * 
	 * @see org.upmc.electisim.knowledge.IStatefulKnowledgeDispenser#getLastSimulationState()
	 */
	@Override
	public SimulationState getLastSimulationState() {
		return stateBuffer.getCurrent();
	}

	/*
	 * (non-Javadoc)
	 * Get the simulation state at the specified index
	 * 
	 * @see org.upmc.electisim.knowledge.IStatefulKnowledgeDispenser#getSimulationState(int)
	 */
	@Override
	public SimulationState getSimulationState(int idx) {
		return stateBuffer.get(idx);
	}

	// TODO : NO ! Maybe remove this method ?
	@Override
	public boolean firstIteration() {
		return stateBuffer.getSize() == 0;
	}

}
