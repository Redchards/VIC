package org.upmc.electisim.output;

import java.io.File;
import java.io.FileNotFoundException;

import org.upmc.electisim.SimulationProfile;

public class SimulationProfileFileWriter extends ASimulationProfileWriter {

	public SimulationProfileFileWriter(String filename) throws FileNotFoundException {
		super(filename);
	}

	public SimulationProfileFileWriter(File file) throws FileNotFoundException {
		super(file);
	}

	public void writeProfile(SimulationProfile profile){
		super.writeProfile(profile);		
	}
}
