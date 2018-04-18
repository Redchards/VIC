package org.upmc.electisim;

import java.util.List;

public class StateBuffer {
	
	private CircularBuffer<SimulationState> buffer;
	private int allowedRewinds = 0;
	
	private static final int DEFAULT_BUFFER_SIZE = 100;


	public StateBuffer(){
		buffer = new CircularBuffer<>(DEFAULT_BUFFER_SIZE);
	}

	public StateBuffer(int bufSize) {
		buffer = new CircularBuffer<>(bufSize);
	}
	
	public StateBuffer(StateBuffer other) {
		
	}




}
