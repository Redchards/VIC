package org.upmc.electisim.output;

import java.io.IOException;
import java.io.OutputStream;

public abstract class AGenericWriter implements AutoCloseable {

	protected OutputStream underlyingStream;
	
	protected AGenericWriter(OutputStream stream){
		underlyingStream = stream;
	}
	
	protected void write(byte[] bytes) throws IOException{
		underlyingStream.write(bytes);
		// underlyingStream.flush();
	}
	
	public void flush() throws IOException {
		underlyingStream.flush();
	}
	
	public OutputStream getOutputStream(){
		return underlyingStream;
	}
	
	@Override
	public void close() throws IOException{
		underlyingStream.close();
	}
	
}
