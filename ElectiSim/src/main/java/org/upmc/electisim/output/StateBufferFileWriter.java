package org.upmc.electisim.output;


import java.io.File;
import java.io.IOException;

import org.upmc.electisim.EmptyBufferException;
import org.upmc.electisim.SimulationState;
import org.upmc.electisim.StateBuffer;

public class StateBufferFileWriter implements AStateBufferWriter {

	private static final String DEFAULT_NAME = "Results";
	private File dir;

	
	public StateBufferFileWriter(){
		this(DEFAULT_NAME);	
	}	
	
	
	public StateBufferFileWriter(String dirname){
		String path = System.getProperty("user.dir");
		this.dir = new File(path+"/"+dirname);
		dir.mkdir();	
	}
	
	
	
	// change param of the function ? stateBuffer should be hidden
	public void writeBuffer(StateBuffer stateBuffer) throws IOException, EmptyBufferException{
		
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
			String filename = dir.getAbsolutePath()+"/iteration_"+count+".csv";   			
			StateFileWriter sfw = new StateFileWriter(filename); 
			try {
				sfw.writeState(state);
			} catch (InvalidStateException e) {
				e.printStackTrace();
			}
			sfw.close();
			currentPointer = (currentPointer+1)%bufferSize;
			count ++;

		}
	}

	
	
		
		
}
