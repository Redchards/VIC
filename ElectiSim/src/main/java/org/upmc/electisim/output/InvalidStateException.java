package org.upmc.electisim.output;

public class InvalidStateException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 72952775880223275L;

	public InvalidStateException() {
		super(getGenericMessage());
	}
	
	public InvalidStateException(Throwable other) {
		super(getGenericMessage(), other);
	}

	private static String getGenericMessage() {
		return "Invalid state : either state is empty or the votes have not been saved";
	}
	
}
