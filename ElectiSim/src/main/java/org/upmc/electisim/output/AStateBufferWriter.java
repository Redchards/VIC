package org.upmc.electisim.output;

import java.io.IOException;

import org.upmc.electisim.StateBuffer;
import org.upmc.electisim.utils.EmptyBufferException;

public interface AStateBufferWriter { 

	public void writeBuffer(StateBuffer stateBuffer) throws IOException, EmptyBufferException, InvalidExtensionException;

}
