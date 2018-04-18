package org.upmc.electisim.output;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.OutputStream;

import org.upmc.electisim.SimulationState;

public abstract class AStateWriter extends AGenericWriter {

	public AStateWriter(String filename) throws FileNotFoundException {
		super(filename);
	}

	public AStateWriter(File file) throws FileNotFoundException {
		super(file);
	}

	protected AStateWriter(OutputStream stream) {
		super(stream);
	}
	
	public void writeState(SimulationState state){
		
	}

	
	
}
