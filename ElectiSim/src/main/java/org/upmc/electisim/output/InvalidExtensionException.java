package org.upmc.electisim.output;

import org.upmc.electisim.GenericMessageException;

public class InvalidExtensionException extends GenericMessageException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8634158085431800971L;

	public InvalidExtensionException(String wrongExtension, String validExtension) {
		super(parseMsg(wrongExtension, validExtension));
	}
	
	public InvalidExtensionException(String wrongExtension, String validExtension, Throwable other) {
		super(parseMsg(wrongExtension, validExtension), other);
	}

	public static String parseMsg(String wrongExtension, String validExtension) {
		return "Expected a " + validExtension + " extension but got a " + wrongExtension + " extension instead !";
	}
	
}
