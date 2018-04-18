package org.upmc.electisim.output;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.upmc.electisim.SimulationState;

public class StateFileWriter extends AStateWriter {

	
	
	
	
	public StateFileWriter(String filename) throws FileNotFoundException {
		super(filename);
	}

	public StateFileWriter(File file) throws FileNotFoundException {
		super(file);
	}

	public void writeState(SimulationState state){
		try {
			super.writeState(state);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
