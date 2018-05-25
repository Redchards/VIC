package org.upmc.electisim;

import java.io.Serializable;

/**
 * An interface for the electable entities of the application. An electable entity doesn't
 * need anything else but to provide a name.
 */
public interface IElectable {
	
	/**
	 * @return The name of the electable entity
	 */
	public String getName();
	
	/**
	 * @return The name of the electable entity
	 */
	public String toString();
}
