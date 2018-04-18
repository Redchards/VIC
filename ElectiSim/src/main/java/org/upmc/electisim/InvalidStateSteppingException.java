package org.upmc.electisim;

public class InvalidStateSteppingException extends GenericMessageException {

	public InvalidStateSteppingException(int numberOfSteps, String direction) {
		super(parseMsg(numberOfSteps, direction));
		// TODO Auto-generated constructor stub
	}

	
	public InvalidStateSteppingException(int numberOfSteps, String direction, Throwable other) {
		super(parseMsg(numberOfSteps, direction), other);
	}
	
	public static String parseMsg(int numberOfSteps, String direction) {
		return "Can't perform " + Integer.toString(numberOfSteps) + " in " + direction + " !";
	}

}
