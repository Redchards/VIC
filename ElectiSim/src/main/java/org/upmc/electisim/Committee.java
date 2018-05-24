package org.upmc.electisim;

import java.util.ArrayList;
import java.util.List;

public class Committee implements IElectable {
	
	private List<IElectable> committee;

	public Committee(List<IElectable> e) {
		this.committee = new ArrayList<>(e);
	}

	
	@Override
	public String getName() {
		return this.committee.toString();
	}

}
