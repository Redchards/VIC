package org.upmc.electisim.input;

import java.io.InputStream;

import org.upmc.electisim.SimulationProfile;

/**
 * An abstract class used as a baseline for our simulation profile reader. A class aiming
 * to read a {@link org.upmc.electisim.SimulationProfile} object from an external source
 * should extend this class.
 */
public abstract class ASimulationSaveReader extends AGenericReader {
	
	/**
	 * Build a simulation profile reader from an input stream
	 * 
	 * @param inputStream the input stream from which the profile will be read
	 */
	protected ASimulationSaveReader(InputStream inputStream) {
		super(inputStream);
	}

	/**
	 * Read the profile from the source and return it
	 * 
	 * @return The read simulation profile
	 * @throws InstantiationException if the profile can't be instantiated
	 * @throws IllegalAccessException if there was an error during the access of the stream
	 * @throws ClassNotFoundException if a class necessary to build to profile was not found
	 */
	public abstract SimulationProfile loadProfile() throws InstantiationException, IllegalAccessException, ClassNotFoundException;
	
}
