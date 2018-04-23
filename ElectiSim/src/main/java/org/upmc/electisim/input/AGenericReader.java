package org.upmc.electisim.input;

import java.io.IOException;
import java.io.InputStream;

public abstract class AGenericReader implements AutoCloseable{
	
	protected InputStream underlyingStream;
	
	protected AGenericReader(InputStream inputStream){
		this.underlyingStream = inputStream;
	}
	
	
	public void close() throws IOException{
		underlyingStream.close();
	}
	
}
