package org.upmc.electisim.output;

import java.io.File;
import java.io.IOException;

import org.upmc.electisim.SimulationState;
import org.upmc.electisim.StateBuffer;

public abstract class AStateBufferWriter /*extends AGenericWriter*/ { //TODO : Ask about that + path

	private static final String DEFAULT_NAME = "Saved StateBuffer";
	private File dir;

	public AStateBufferWriter(String dirName) {
		String path = System.getProperty("user.dir");
		this.dir = new File(path+"/"+dirName);
		dir.mkdir();
		
	}
	
	public AStateBufferWriter(){
		this(DEFAULT_NAME);
	}

	/*
	public AStateBufferWriter(File file) throws FileNotFoundException {
		super(file);
		
	}	
	
	protected AStateBufferWriter(OutputStream stream) {
		super(stream);
	}
	*/
	public void writeBuffer(StateBuffer stateBuffer){
		
		//TODO 19.04.2018 : The elected committee isn't saved 
		
		int bufferSize = stateBuffer.getSize();
		int count = 0;
		int currentPointer = stateBuffer.getCurrentPointer()%bufferSize;
		
		while(count<bufferSize){
			SimulationState state = stateBuffer.get(currentPointer);
			String filename = dir.getAbsolutePath()+"/iteration_"+count+".csv";   
			System.out.println("Filename =="+filename);
			try {
				StateFileWriter sfw = new StateFileWriter(filename);
				sfw.writeState(state);
				sfw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			currentPointer = (currentPointer+1)%bufferSize;
			count ++;

		}
	}

}
