package org.upmc.electisim.knowledge;

import org.upmc.electisim.SimulationState;
import org.upmc.electisim.StateBuffer;

public class StatefulKnowledgeDispenser implements IStatefulKnowledgeDispenser {
	
	private StateBuffer stateBuffer;
	
	public StatefulKnowledgeDispenser(StateBuffer stateBuffer) {
		this.stateBuffer = stateBuffer;
	}

	@Override
	public SimulationState getLastSimulationState() {
		return stateBuffer.getCurrent();
	}

	@Override
	public SimulationState getSimulationState(int idx) {
		return stateBuffer.get(idx);
	}

	@Override
	public boolean firstIteration() {
		return stateBuffer.getSize() == 0;
	}

}
