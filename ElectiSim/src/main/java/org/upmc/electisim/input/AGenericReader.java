package org.upmc.electisim.input;

import java.io.IOException;
import java.io.InputStream;

public abstract class AGenericReader {
	
	protected InputStream inputStream;
	
	protected AGenericReader(InputStream inputStream){
		this.inputStream = inputStream;
	}
	
	
	public void close() throws IOException{
		inputStream.close();
	}
	
}
