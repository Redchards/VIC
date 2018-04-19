package org.upmc.electisim.output;

import java.io.IOException;

import org.upmc.electisim.StateBuffer;

public interface AStateBufferWriter { 

	public void writeBuffer(StateBuffer stateBuffer) throws IOException;

}
