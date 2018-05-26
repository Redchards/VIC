package org.upmc.electisim;

import java.util.Arrays;

public class HashValue {
	private byte[] value;
	
	public HashValue(byte[] hashValue) {
		this.value = hashValue.clone();
	}
	
	public HashValue(HashValue other) {
		this.value = other.value.clone();
	}
	
	public byte[] get() {
		return value;
	}
	
	public void set(byte[] hashValue) {
		this.value = hashValue;
	}
	
	public boolean equals(byte[] other) {
		return Arrays.equals(value, other);
	}
}
