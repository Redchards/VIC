package org.upmc.electisim;

import java.util.List;

/**
 * An exception class used when the preferences of an agent are inconsistent.
 * @see org.upmc.electisim.Preferences
 * @see org.upmc.electisim.PreferencesValidator
 */
public class PreferencesInconsistencyException extends GenericMessageException {
	/**
     * Constructs a new exception from the candidate list and the agent throwing the exception
     * 
     * @param candidates the preferences list
     * @param agent the agent emitting the exception
     */
    public PreferencesInconsistencyException(List<IElectable> candidates, Agent agent) {
    	super(parseMessage(candidates, agent));

    }        

    /**
     * Constructs a new exception, concatenating the new message with the other exception message
     * 
     * @param candidates the preferences list
     * @param agent the agent emitting the exception
     * @param other the other exception
     */
    public PreferencesInconsistencyException(List<IElectable> candidates, Agent agent, Throwable other) {
    	super(parseMessage(candidates, agent), other);
    }
    
    /*
     * (non-javadoc)
     * Build the message from the candidate list and the agent
     * 
     * @param candidates the preference list
     * @param agent the agent to which to preferences belong
     */
    private static String parseMessage(List<IElectable> candidates, Agent agent) {
    	if(candidates.size() == 1) {
        	return "Inconsistency in the preferences of agent '" + agent.getName() +
        			"' : the candidate '" + candidates.get(0).getName() +"' does not exist !";

    	}
    	
    	String namesStr = "";
    	for(IElectable c : candidates) {
    		namesStr += c.getName();
    		namesStr += ",";
    	}
    	return "Inconsistency in the preferences of agent '" + agent.getName() +
    			"' : the candidates '" + namesStr.substring(0, namesStr.length() - 1) +"' does not exist !";
    }
}
