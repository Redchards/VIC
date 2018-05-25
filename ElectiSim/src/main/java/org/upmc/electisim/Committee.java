package org.upmc.electisim;

import java.util.ArrayList;
import java.util.List;

/**
 * An electable entity representing a committee. A committee is simply a list of other electable
 * entities, which can be of any type provided that the implement the {@link org.upmc.electisim.Committee}
 * interface.
 */
public class Committee implements IElectable {
	
	/*
	 * (non-javadoc)
	 * The list of electable entities representing the committee
	 */
	private List<IElectable> committee;

	/**
	 * Build a committee from a list of electable entities
	 * 
	 * @param committee the committee to copy
	 */
	public Committee(List<IElectable> committee) {
		this.committee = new ArrayList<>(committee);
	}

	/*
	 * (non-Javadoc)
	 * @return The name of the committee, which is simply the stringized list of electable
	 * entities
	 * @see org.upmc.electisim.IElectable#getName()
	 */
	@Override
	public String getName() {
		return this.committee.toString();
	}
	
	/*
	 * (non-Javadoc)
	 * @return The name of the committee, which is simply the stringized list of electable
	 * entities
	 * @see org.upmc.electisim.IElectable#getName()
	 */
	@Override
	public String toString() {
		return getName();
	}

}
