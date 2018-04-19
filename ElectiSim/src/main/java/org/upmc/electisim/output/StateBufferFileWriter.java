package org.upmc.electisim.output;


import org.upmc.electisim.StateBuffer;

public class StateBufferFileWriter extends AStateBufferWriter {

	
	/*
	public StateBufferFileWriter(String filename) throws FileNotFoundException {
		super(filename);
	}
	
	public StateBufferFileWriter(File file) throws FileNotFoundException {
		super(file);
		
	}*/
	
	
	public void writeBuffer(StateBuffer stateBuffer){
		super.writeBuffer(stateBuffer);
	}
	
}
