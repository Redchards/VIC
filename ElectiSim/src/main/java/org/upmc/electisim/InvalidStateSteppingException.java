package org.upmc.electisim;

public class InvalidStateSteppingException extends GenericMessageException {

	/**
	 * Builds an exception from a number of step and the direction in which to step in the
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
	 * Builds an exception from a number of step and the direction in which to step in the
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
	 * Parses the error message
	 * 
	 * @param numberOfSteps the number of steps to make
	 * @param direction the direction in which to step
	 */
	private static String parseMsg(int numberOfSteps, String direction) {
		return "Can't perform " + Integer.toString(numberOfSteps) + " in " + direction + " !";
	}

}
