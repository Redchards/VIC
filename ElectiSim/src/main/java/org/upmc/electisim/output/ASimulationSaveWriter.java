package org.upmc.electisim.output;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.upmc.electisim.SimulationProfile;

public abstract class ASimulationSaveWriter extends AGenericWriter {

	public ASimulationSaveWriter(String filename) throws FileNotFoundException {
		this(new FileOutputStream(filename));
	}

	public ASimulationSaveWriter(File file) throws FileNotFoundException {
		this(new FileOutputStream(file));
	}
	
	protected ASimulationSaveWriter(OutputStream outputStream){
		super(outputStream);
	}

	public abstract void writeProfile(SimulationProfile profile) throws IOException;
	

}
