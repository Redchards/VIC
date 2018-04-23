package org.upmc.electisim;

import java.io.Serializable;

public class Candidate implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -171544863965648891L;
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
