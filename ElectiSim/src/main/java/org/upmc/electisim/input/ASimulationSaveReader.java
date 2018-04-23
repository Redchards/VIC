package org.upmc.electisim.input;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.upmc.electisim.SimulationProfile;

public abstract class ASimulationSaveReader extends AGenericReader {
	
	
	
	public ASimulationSaveReader(String filename) throws FileNotFoundException{
		this(new FileInputStream(filename));
	}
	
	public ASimulationSaveReader(File file) throws FileNotFoundException{
		this(new FileInputStream(file));
	}
		
	protected ASimulationSaveReader(InputStream inputStream) {
		super(inputStream);
	}

	public abstract SimulationProfile loadProfile() throws InstantiationException, IllegalAccessException, ClassNotFoundException;
	
}
