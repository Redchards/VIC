package gui.upmc.electisim;

import java.util.List;
import java.util.Optional;

import org.upmc.electisim.Agent;
import org.upmc.electisim.IElectable;
import org.upmc.electisim.PreferenceType;


public class ConfigurationUtils {
	public static Optional<Agent> showAgentConfigurationBox(Agent agent, PreferenceType type, List<IElectable> candidateList) throws AgentConfigurationException {
		AgentConfigurationBox confBox = new AgentConfigurationBox(agent, type, candidateList);
		confBox.showAndWait();
		return confBox.getAgent();
	}
	
	public static Optional<IElectable> showCandidateConfigurationBox(IElectable candidate, List<IElectable> candidateList) throws CandidateConfigurationException {
		CandidateConfigurationBox confBox = new CandidateConfigurationBox(candidate, candidateList);
		confBox.showAndWait();
		return confBox.getCandidate();
	}
}
