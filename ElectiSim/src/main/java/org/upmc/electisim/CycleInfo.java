package org.upmc.electisim;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// TODO : Why do we even have a singular hash value in there ?
public class CycleInfo {

	private int start;
	private int end;
	
	public CycleInfo(int start, int end) {
		this.start = start;
		this.end = end;
	}
	
	public int getStart() {
		return start;
	}
	
	public int getEnd() {
		return end;
	}
	
	public int getCycleLength() {
		return Math.abs(end - start);
	}
	
	@Override
	public String toString() {
		return "start  : " + Integer.toString(start) + "\n"
			 + "end    : " + Integer.toString(end) + "\n"
			 + "length : " + Integer.toString(getCycleLength());
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj == null) {
			return false;
		}
		CycleInfo other = (CycleInfo) obj;
		return (getStart() == other.getStart()
			 && getEnd() == other.getEnd());
	}
}
