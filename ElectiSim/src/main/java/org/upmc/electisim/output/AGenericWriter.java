package org.upmc.electisim.output;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public abstract class AGenericWriter {

	private OutputStream underlyingStream;
	//protected OutputStream underlyingStream;
	
	public AGenericWriter(String filename) throws FileNotFoundException{
		
		this(new FileOutputStream(filename));
		
	}
	
	public AGenericWriter(File file) throws FileNotFoundException{
		this(new FileOutputStream(file, false));		
	}
	
	protected AGenericWriter(OutputStream stream){
		underlyingStream = stream;
	}
	
	public void write(String string){
		try {
			underlyingStream.write(string.getBytes());
			underlyingStream.flush();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public OutputStream getOutputStream(){
		return underlyingStream;
	}
	
	public void close() throws IOException{
		underlyingStream.close();
		
	}
	
}
