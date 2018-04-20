package org.upmc.electisim.input;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import org.upmc.electisim.SimulationProfile;

public abstract class ASimulationProfileReader extends AGenericReader {

	
	public ASimulationProfileReader(String filename) throws FileNotFoundException{
		this(new File(filename));
	}
	
	public ASimulationProfileReader(File file) throws FileNotFoundException{
		this(new FileInputStream(file));
	}
		
	protected ASimulationProfileReader(InputStream inputStream) {
		super(inputStream);
	}

	public abstract SimulationProfile loadProfile();
	
}
