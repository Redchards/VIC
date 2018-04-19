package org.upmc.electisim.output;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.upmc.electisim.SimulationState;
import org.upmc.electisim.StateBuffer;

public abstract class AStateBufferWriter extends AGenericWriter { 

	private static final String DEFAULT_NAME = "Results";
	private File dir;

	
	public AStateBufferWriter(File file) throws FileNotFoundException{
		this(file, DEFAULT_NAME);	
	}	
	
	public AStateBufferWriter(String filename) throws FileNotFoundException {
		this(filename, DEFAULT_NAME);	
	}
	
	
	public AStateBufferWriter(File file, String dirname) throws FileNotFoundException {
		this(new FileOutputStream(file), dirname);				
	}
	
	public AStateBufferWriter(String filename, String dirname) throws FileNotFoundException {
		this(new FileOutputStream(filename), dirname);
			
	}
	
	protected AStateBufferWriter(OutputStream outputStream, String dirname){
		super(outputStream);
		String path = System.getProperty("user.dir");
		this.dir = new File(path+"/"+dirname);
		dir.mkdir();	
	}
	
	
	
	
	public void writeBuffer(StateBuffer stateBuffer){
		
		//TODO 19.04.2018 : The elected committee isn't saved 
		
		int bufferSize = stateBuffer.getSize();
		int count = 0;
		int currentPointer = stateBuffer.getCurrentPointer()%bufferSize; // TODO 19.04.2018 : Use iterator instead !!! 
		
		while(count<bufferSize){
			SimulationState state = stateBuffer.get(currentPointer);
			String filename = dir.getAbsolutePath()+"/iteration_"+count+".csv";   
			System.out.println("Filename =="+filename);
			try {
				StateFileWriter sfw = new StateFileWriter(super.underlyingStream); // TODO 19.04.2018 : ask about this
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
