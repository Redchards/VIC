package org.upmc.electisim;

import java.util.EventListener;

public class ListenerNotFoundException extends GenericMessageException {

	public ListenerNotFoundException(EventListener listener) {
		super(parseMessage(listener));
		// TODO Auto-generated constructor stub
	}
	
	public ListenerNotFoundException(EventListener listener, Throwable other) {
		super(parseMessage(listener), other);
		// TODO Auto-generated constructor stub
	}
	
	private static String parseMessage(EventListener listener) {
		return "Can't find the listener '" + listener.toString() + "' !";
	}

}
