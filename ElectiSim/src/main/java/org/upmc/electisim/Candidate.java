package org.upmc.electisim;

import java.io.Serializable;

public class Candidate {
	
	private String name;
	

	public Candidate(String name) {
		this.setName(name);
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public String toString() {
		return getName();
	}
}
