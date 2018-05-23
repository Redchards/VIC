package gui.upmc.electisim;

import org.upmc.electisim.GenericMessageException;
import org.upmc.electisim.IElectable;

public class CandidateConfigurationException extends GenericMessageException {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2232920377703904732L;

	public CandidateConfigurationException(IElectable candidate, String msg) {
		super(parseMessage(candidate, msg));
	}
	
	public CandidateConfigurationException(IElectable candidate, String msg, Throwable other) {
		super(parseMessage(candidate, msg), other);
	}
	
	private static String parseMessage(IElectable candidate, String msg) {
		return "An error arose during the configuration of the candidate '" + candidate.getName() 
		+ "'. Reason : " + msg;
	}
}
