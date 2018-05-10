package org.upmc.electisim;

import java.util.ArrayList;
import java.util.List;

public class Committee implements IElectable {
	
	private List<Candidate> committee;

	public Committee(List<Candidate> committee) {
		this.committee = new ArrayList<>(committee);
	}

	@Override
	public String getName() {
		return this.committee.toString();
	}

}
