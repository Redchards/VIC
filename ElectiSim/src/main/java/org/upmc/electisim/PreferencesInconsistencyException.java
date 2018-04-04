package org.upmc.electisim;

import java.util.List;

public class PreferencesInconsistencyException extends GenericMessageException {
	/**
     * Constructs a new exception from the desired message.
     * @param msg the message to store inside the exception.
     */
    public PreferencesInconsistencyException(List<Candidate> candidates) {
    	super(parseMessage(candidates));

    }        

    /**
     * Constructs a new exception, concatenating the new message with the other exception message.
     * @param msg the message to which the other exception message will be appended.
     * @param other the other exception.
     */
    public PreferencesInconsistencyException(List<Candidate> candidates, Throwable other) {
    	super(parseMessage(candidates) + " (" + other.getMessage() + ")", other);
    }
    
    private static String parseMessage(List<Candidate> candidates) {
    	if(candidates.size() == 1) {
        	return "The candidate '" + candidates.get(0).getName() +"' does not exist !";

    	}
    	
    	String namesStr = "";
    	for(Candidate c : candidates) {
    		namesStr += c.getName();
    		namesStr += ",";
    	}
    	return "The candidates '" + namesStr.substring(0, namesStr.length() - 1) +"' does not exist !";
    }
}
