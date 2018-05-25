package org.upmc.electisim.input;

import java.io.IOException;
import java.io.InputStream;

/**
 * A generic reader class implementing the {@link java.io.AutoCloseable} interface and reading
 * from an {@link java.io.InputStream}.
 */
public abstract class AGenericReader implements AutoCloseable {
	
	/*
	 * (non-Javadoc)
	 * The underlying input stream
	 */
	protected InputStream underlyingStream;
	
	/**
	 * Buid a generic reader using an input stream
	 * 
	 * @param inputStream the input stream to be used
	 */
	protected AGenericReader(InputStream inputStream){
		this.underlyingStream = inputStream;
	}
	
	/*
	 * (non-Javadoc)
	 * Close the reader
	 * 
	 * @see java.lang.AutoCloseable#close()
	 */
	@Override
	public void close() throws IOException{
		underlyingStream.close();
	}
	
}
