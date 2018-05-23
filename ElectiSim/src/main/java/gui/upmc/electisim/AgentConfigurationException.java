package gui.upmc.electisim;

import org.upmc.electisim.Agent;
import org.upmc.electisim.GenericMessageException;

public class AgentConfigurationException extends GenericMessageException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -304435262138642664L;

	public AgentConfigurationException(Agent agent, String msg) {
		super(parseMessage(agent, msg));
	}
	
	public AgentConfigurationException(Agent agent, String msg, Throwable other) {
		super(parseMessage(agent, msg), other);
	}
	
	private static String parseMessage(Agent agent, String msg) {
		return "An error arose during the configuration of the agent '" + agent.getName() 
		+ "'. Reason : " + msg;
	}

}
