package org.upmc.electisim;

import java.util.List;

public class StateBuffer {
	
	private CircularBuffer<SimulationState> buffer;
	private int allowedRewinds = 0;
	
	private static final int DEFAULT_BUFFER_SIZE = 100;


	public StateBuffer(){
		buffer = new CircularBuffer<SimulationState>(DEFAULT_BUFFER_SIZE);
	}







}
