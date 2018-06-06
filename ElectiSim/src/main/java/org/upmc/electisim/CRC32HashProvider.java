package org.upmc.electisim;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;
import java.util.zip.CRC32;

public class CRC32HashProvider implements IHashProvider {

	@Override
	public HashValue hashCode(Object o) {
		CRC32 crc = new CRC32();
		HashValue value = null;
		
		ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
		try {
			ObjectOutputStream out = new ObjectOutputStream(byteOut);
			out.writeObject(o);
			crc.update(byteOut.toByteArray());
			ByteBuffer bf = ByteBuffer.allocate(8);
			bf.putLong(crc.getValue());
			
			value = new HashValue(bf.array());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	
		return value;
	}

}
