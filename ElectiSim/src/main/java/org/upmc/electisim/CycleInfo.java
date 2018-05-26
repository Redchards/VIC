package org.upmc.electisim;

public class CycleInfo {

	private int start;
	private int end;
	private HashValue hashValue;
	
	public CycleInfo(int start, int end, HashValue hashValue) {
		this.start = start;
		this.end = end;
		this.hashValue = hashValue;
	}
	
	public int getStart() {
		return start;
	}
	
	public int getEnd() {
		return end;
	}
	
	public HashValue getHashValue() {
		return hashValue;
	}
	
	public int getCycleLength() {
		return Math.abs(end - start);
	}
	
	@Override
	public String toString() {
		return "start : " + Integer.toString(start) + "\n"
			 + "end   : " + Integer.toString(end);
	}
}
