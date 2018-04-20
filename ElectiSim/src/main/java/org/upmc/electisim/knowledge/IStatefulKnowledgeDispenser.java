package org.upmc.electisim.knowledge;

import org.upmc.electisim.SimulationState;

public interface IStatefulKnowledgeDispenser extends IZeroKnowledgeDispenser {
	public SimulationState getLastSimulationState();
	public SimulationState getSimulationState(int idx);
	public boolean firstIteration();
}
