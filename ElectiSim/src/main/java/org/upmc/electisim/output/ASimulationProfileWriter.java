package org.upmc.electisim.output;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.OutputStream;

import org.upmc.electisim.SimulationProfile;

public abstract class ASimulationProfileWriter extends AGenericWriter {

	public ASimulationProfileWriter(String filename) throws FileNotFoundException {
		super(filename);
	}

	public ASimulationProfileWriter(File file) throws FileNotFoundException {
		super(file);
	}

	protected ASimulationProfileWriter(OutputStream stream) {
		super(stream);
	}
	
	public void writeProfile(SimulationProfile profile){
		
	}

}
