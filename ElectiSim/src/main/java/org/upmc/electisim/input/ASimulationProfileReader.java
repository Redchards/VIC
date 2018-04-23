package org.upmc.electisim.input;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.upmc.electisim.SimulationProfile;
import org.upmc.electisim.output.SimulationProfileObjectWriter;

public abstract class ASimulationProfileReader extends AGenericReader {
	
	
	public ASimulationProfileReader() throws FileNotFoundException{
		this(new FileInputStream(SimulationProfileObjectWriter.DEFAULT_PROFILE_FILENAME));
	}
	
	public ASimulationProfileReader(String filename) throws FileNotFoundException{
		this(new FileInputStream(filename));
	}
	
	public ASimulationProfileReader(File file) throws FileNotFoundException{
		this(new FileInputStream(file));
	}
		
	protected ASimulationProfileReader(InputStream inputStream) {
		super(inputStream);
	}

	public abstract SimulationProfile loadProfile() throws ClassNotFoundException, IOException;
	
}
