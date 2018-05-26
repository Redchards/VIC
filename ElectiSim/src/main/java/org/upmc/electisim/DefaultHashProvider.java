package org.upmc.electisim;

import java.nio.ByteBuffer;

public class DefaultHashProvider implements IHashProvider {
	
	@Override
	public HashValue hashCode(Object o) {
		int hashCode = o.hashCode();
		
		ByteBuffer bf = ByteBuffer.allocate(4);
		bf.putInt(hashCode);
		
		return new HashValue(bf.array());
	}

}
