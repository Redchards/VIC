package org.upmc.electisim.output;


import java.io.File;
import java.io.IOException;

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
	
	
	
	
	public void writeBuffer(StateBuffer stateBuffer) throws IOException{
		
		//TODO 19.04.2018 : The elected committee isn't saved 
		
		int bufferSize = stateBuffer.getSize();
		int count = 0;
		int currentPointer = stateBuffer.getCurrentPointer()%bufferSize; // TODO 19.04.2018 : Use iterator instead !!!
		
		
		while(count<bufferSize){
			SimulationState state = stateBuffer.get(currentPointer);
			String filename = dir.getAbsolutePath()+"/iteration_"+count+".csv";   			
			StateFileWriter sfw = new StateFileWriter(filename); 
			sfw.writeState(state);
			sfw.close();
			currentPointer = (currentPointer+1)%bufferSize;
			count ++;

		}
	}

	
	
		
		
}
