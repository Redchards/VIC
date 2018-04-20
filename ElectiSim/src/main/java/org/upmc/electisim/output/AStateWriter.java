package org.upmc.electisim.output;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.upmc.electisim.SimulationState;

public abstract class AStateWriter extends AGenericWriter {
	
	public AStateWriter(String filename) throws FileNotFoundException {
		this(new FileOutputStream(filename));
	}

	public AStateWriter(File file) throws FileNotFoundException {
		this(new FileOutputStream(file));
	}

	protected AStateWriter(OutputStream outputStream) throws FileNotFoundException { 
		super(outputStream);
	}
	
	
	public abstract void writeState(SimulationState state)throws IOException, InvalidStateException; 

}
