package org.upmc.electisim;

import java.util.ArrayList;
import java.util.List;

public class SimpleAgentGenerator implements IAgentGenerator {

	private static final String DEFAULT_AGENT_BASE_NAME = "A";
	
	@Override
	public Agent generate(List<Agent> agentList, PreferenceType type) {
		int candidateNumber = 1;
		String currentAgentName = null;
		boolean needsRecheck = true;
		
		while(needsRecheck) {
			needsRecheck = false;
			currentAgentName = (DEFAULT_AGENT_BASE_NAME + Integer.toString(candidateNumber));
			for(Agent a : agentList) {
				if(currentAgentName.equals(a.getName())) {
					needsRecheck = true;
					candidateNumber++;
					break;
				}
			}
		}
		
		return new Agent(currentAgentName, new Preferences(type, new ArrayList<>()));	
	}

}
