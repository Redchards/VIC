package org.upmc.electisim.output;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public abstract class AGenericWriter {

	private OutputStream underlyingStream;
	
	
	public AGenericWriter(String filename) throws FileNotFoundException{
		
		this(new FileOutputStream(filename));
		
	}
	
	public AGenericWriter(File file) throws FileNotFoundException{
		this(new FileOutputStream(file));		
	}
	
	protected AGenericWriter(OutputStream stream){
		underlyingStream = stream;
	}
	
	
	public void close(){
		try {
			underlyingStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
