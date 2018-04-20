package org.upmc.electisim.knowledge;

import org.upmc.electisim.SimulationState;

public interface IStatefulKnowledgeDispenser {
	public SimulationState getLastSimulationState();
	public SimulationState getSimulationState(int idx);
}
