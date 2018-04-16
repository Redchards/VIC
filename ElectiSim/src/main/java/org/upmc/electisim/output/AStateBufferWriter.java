package org.upmc.electisim.output;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.OutputStream;

import org.upmc.electisim.StateBuffer;

public abstract class AStateBufferWriter extends AGenericWriter {

	

	public AStateBufferWriter(String filename) throws FileNotFoundException {
		super(filename);
		
	}

	public AStateBufferWriter(File file) throws FileNotFoundException {
		super(file);
		
	}	
	
	protected AStateBufferWriter(OutputStream stream) {
		super(stream);
	}
	
	public void writeBuffer(StateBuffer state){
		
	}
}
