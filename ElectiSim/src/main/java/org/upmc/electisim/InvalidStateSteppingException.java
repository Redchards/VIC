package org.upmc.electisim;

/**
 * An exception class thrown when an invalid stepping operation occurs
 * 
 * @see org.upmc.electisim.SimulationEngine
 */
public class InvalidStateSteppingException extends GenericMessageException {

	/**
	 * Generated UID
	 */
	private static final long serialVersionUID = 3036921673009823679L;

	/**
	 * Build an exception from a number of step and the direction in which to step in the
	 * state buffer
	 * 
	 * @param numberOfSteps the number of steps to make
	 * @param direction the direction in which to step
	 */
	public InvalidStateSteppingException(int numberOfSteps, String direction) {
		super(parseMsg(numberOfSteps, direction));
		// TODO Auto-generated constructor stub
	}

	/**
	 * Build an exception from a number of step and the direction in which to step in the
	 * state buffer. Takes another exception in parameter and concatenates the messages
	 * 
	 * @param numberOfSteps the number of steps to make
	 * @param direction the direction in which to step
	 * @param other the exception which cause this one
	 */
	public InvalidStateSteppingException(int numberOfSteps, String direction, Throwable other) {
		super(parseMsg(numberOfSteps, direction), other);
	}
	
	/*
	 * (non-javadoc)
	 * Parse the error message
	 * 
	 * @param numberOfSteps the number of steps to make
	 * @param direction the direction in which to step
	 */
	private static String parseMsg(int numberOfSteps, String direction) {
		return "Can't perform " + Integer.toString(numberOfSteps) + " in " + direction + " !";
	}

}
