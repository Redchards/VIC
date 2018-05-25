package org.upmc.electisim;

import java.util.EventListener;

/**
 * An exception which might be thrown when a listener is not found but requested for.
 * @see org.upmc.electisim.SimulationEngine
 */
public class ListenerNotFoundException extends GenericMessageException {

	/*
	 * (non-javadoc)
	 * Generated serial UID
	 */
	private static final long serialVersionUID = -3960975975774621036L;

	/**
	 * Build an exception from a listener
	 * 
	 * @param listener the listener causing the exception to be thrown
	 */
	public ListenerNotFoundException(EventListener listener) {
		super(parseMessage(listener));
	}
	
	/**
	 * Build an exception from a listener and another exception
	 * 
	 * @param listener the listener causing the exception to be thrown
	 * @param other the higher level exception causing this exception to be thrown
	 */
	public ListenerNotFoundException(EventListener listener, Throwable other) {
		super(parseMessage(listener), other);
	}
	
	/*
	 * (non-javadoc)
	 * Builds an error message from a listener
	 * 
	 * @param listener the listener to use
	 * @return The error message
	 */
	private static String parseMessage(EventListener listener) {
		return "Can't find the listener '" + listener.toString() + "' !";
	}

}
