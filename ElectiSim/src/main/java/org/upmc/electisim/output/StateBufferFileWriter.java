package org.upmc.electisim.output;


import java.io.File;
import java.io.FileNotFoundException;

import org.upmc.electisim.StateBuffer;

public class StateBufferFileWriter extends AStateBufferWriter {

	private static final String DEFAULT_NAME = "Results";
	private File dir;

	
	public StateBufferFileWriter(File file, String dirName) throws FileNotFoundException {
		super(file, dirName);
	}
	
	public StateBufferFileWriter(File file) throws FileNotFoundException {
		super(file);
		
	}
	
	public StateBufferFileWriter(String filename, String dirName) throws FileNotFoundException {
		super(filename, dirName);
	}
	
	public StateBufferFileWriter(String filename) throws FileNotFoundException {
		super(filename);
		
	}
	
	public void writeBuffer(StateBuffer stateBuffer){
		super.writeBuffer(stateBuffer);
	}
	
}
