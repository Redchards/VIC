package org.upmc.electisim;

public class EmptyBufferException extends GenericMessageException {

	public EmptyBufferException() {
		super(getGenericMessage());
	}
	
	public EmptyBufferException(Throwable other) {
		super(getGenericMessage(), other);
	}

	private static String getGenericMessage() {
		return "No more elements in the buffer";
	}
	
}
