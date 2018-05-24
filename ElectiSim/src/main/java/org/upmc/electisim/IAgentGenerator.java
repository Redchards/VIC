package org.upmc.electisim;

import java.util.List;

public interface IAgentGenerator {
	public Agent generate(List<Agent> agentList, PreferenceType type);
}
