package org.upmc.electisim;

import java.util.List;

public class PreferencesInconsistencyException extends GenericMessageException {
	/**
     * Constructs a new exception from the desired message.
     * @param msg the message to store inside the exception.
     */
    public PreferencesInconsistencyException(List<Candidate> candidates, Agent agent) {
    	super(parseMessage(candidates, agent));

    }        

    /**
     * Constructs a new exception, concatenating the new message with the other exception message.
     * @param msg the message to which the other exception message will be appended.
     * @param other the other exception.
     */
    public PreferencesInconsistencyException(List<Candidate> candidates, Agent agent, Throwable other) {
    	super(parseMessage(candidates, agent), other);
    }
    
    private static String parseMessage(List<Candidate> candidates, Agent agent) {
    	if(candidates.size() == 1) {
        	return "Inconsistency in the preferences of agent '" + agent.getName() +
        			"' : the candidate '" + candidates.get(0).getName() +"' does not exist !";

    	}
    	
    	String namesStr = "";
    	for(Candidate c : candidates) {
    		namesStr += c.getName();
    		namesStr += ",";
    	}
    	return "Inconsistency in the preferences of agent '" + agent.getName() +
    			"' : the candidates '" + namesStr.substring(0, namesStr.length() - 1) +"' does not exist !";
    }
}
