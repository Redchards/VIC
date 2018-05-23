package gui.upmc.electisim;

import org.upmc.electisim.GenericMessageException;

public class SimulationProfileConfigurationException extends GenericMessageException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8099126985380355234L;

	public SimulationProfileConfigurationException(String msg) {
		super(parseMessage(msg));
	}
	public SimulationProfileConfigurationException(String msg, Throwable other) {
		super(parseMessage(msg), other);
		// TODO Auto-generated constructor stub
	}

	private static String parseMessage(String msg) {
		return "Unable to create the profile from the given configuration. " + msg;
	}
}
