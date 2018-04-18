package org.upmc.electisim.output;

import java.io.File;
import java.io.FileNotFoundException;

import org.upmc.electisim.StateBuffer;

public class StateBufferFileWriter extends AStateBufferWriter {

	

	public StateBufferFileWriter(String filename) throws FileNotFoundException {
		super(filename);
	}
	
	public StateBufferFileWriter(File file) throws FileNotFoundException {
		super(file);
		
	}
	
	public void writeBuffer(StateBuffer state){
		
	}
	
}
