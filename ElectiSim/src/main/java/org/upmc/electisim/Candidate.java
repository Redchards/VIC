package org.upmc.electisim;

/**
 * A class representing a Candidate, one of the electable entities.
 * It's the most common type of electable entity, favored by many strategies and
 * a large number of voting rules.
 */
public class Candidate implements IElectable {

	/*
	 * (non-javadoc)
	 * The name of the candidate
	 */
	private String name;
	
	/**
	 * Build a candidate from a name
	 * @param name the name of the candidate
	 */
	public Candidate(String name) {
		this.name = name;
	}
	
	/*
	 * (non-Javadoc)
	 * @return The name of the candidate
	 * 
	 * @see org.upmc.electisim.IElectable#getName()
	 */
	@Override
	public String getName() {
		return this.name;
	}
	
	/*
	 * (non-Javadoc)
	 * @return The name of the candidate
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return getName();
	}

}
