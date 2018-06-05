package org.upmc.electisim.output;


import java.io.File;
import java.io.IOException;

import org.upmc.electisim.SimulationState;
import org.upmc.electisim.StateBuffer;
import org.upmc.electisim.utils.EmptyBufferException;

public class StateBufferFileWriter implements AStateBufferWriter, AutoCloseable {

	private static final String DEFAULT_NAME = "Results";
	private File dir;

	
	public StateBufferFileWriter(){
		this(DEFAULT_NAME);	
	}	
	
	
	public StateBufferFileWriter(String dirname){
		this.dir = new File(dirname);
		dir.mkdir();	
	}
	
	
	
	// change param of the function ? stateBuffer should be hidden
	public void writeBuffer(StateBuffer stateBuffer) throws IOException, EmptyBufferException, InvalidExtensionException{
		
		//TODO 19.04.2018 : The elected committee isn't saved 
		// change emptyBufException generic msg ?
		 
		if(stateBuffer == null || stateBuffer.getSize() == 0){
			throw new EmptyBufferException();
		}
		
		int bufferSize = stateBuffer.getSize();
		int count = 0;
		int currentPointer = stateBuffer.getCurrentPointer()%bufferSize; // TODO 19.04.2018 : Use iterator instead !!!
		
		
		while(count<bufferSize){
			SimulationState state = stateBuffer.get(currentPointer);
			String filename = dir.getAbsolutePath()+"/state_at_iteration_"+count+".csv";   			
			try(StateFileWriter sfw = new StateFileWriter(filename)) 
			{
				sfw.writeState(state);
			} 
			currentPointer = (currentPointer+1)%bufferSize;
			count ++;

		}
	}


	@Override
	public void close() throws Exception {
		// FOO
	}

	
	
		
		
}
