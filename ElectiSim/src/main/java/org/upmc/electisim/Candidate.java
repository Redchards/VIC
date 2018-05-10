package org.upmc.electisim;

public class Candidate implements IElectable {

	private String name;
	
	public Candidate(String name) {
		this.name = name;
	}
	
	@Override
	public String getName() {
		return this.name;
	}
	
	@Override
	public String toString() {
		return getName();
	}

}
