package org.upmc.electisim;

public class StateBuffer extends CircularBuffer<SimulationState> {
		
	private static final int DEFAULT_BUFFER_SIZE = 100;
	private int statePointer = 0;


	public StateBuffer(){
		super(DEFAULT_BUFFER_SIZE);
	}

	public StateBuffer(int bufSize) {
		super(bufSize);
	}
	
	public StateBuffer(StateBuffer other) {
		super(other);
		this.statePointer = other.statePointer;
	}
	
	public SimulationState getCurrent() {
		return this.buffer.get(statePointer);
	}
	
	public void rewind(int steps) throws InvalidStateSteppingException {
		step(steps, STEPPING_DIRECTION.BACKWARD);
	}
	
	public void rewindStep() throws InvalidStateSteppingException {
		this.rewind(1);
	}
	
	public void advance(int steps) throws InvalidStateSteppingException {
		step(steps, STEPPING_DIRECTION.FORWARD);
	}
	
	public void advanceStep() throws InvalidStateSteppingException {
		this.advance(1);
	}
	
	private void checkSteppingValidity(int steps, STEPPING_DIRECTION direction) throws InvalidStateSteppingException {
		int offset = 0;
		
		switch(direction) {
		case FORWARD:
			offset = steps;
		case BACKWARD:
			offset = -steps;
		}
		
		if(steps > getCapacity()) {
			throw new InvalidStateSteppingException(steps, "backward");
		}
		if(statePointer > currentPointer) {
			if(wrapIndex(statePointer + offset) >= currentPointer) {
				throw new InvalidStateSteppingException(steps, "backward");
			}
		}
		if(statePointer + offset >= currentPointer) {
			throw new InvalidStateSteppingException(steps, "backward");
		}
	}
	
	// TODO : maybe make this public along with the STEPPING_DIRECTION enum ?
	private void step(int steps, STEPPING_DIRECTION direction) throws InvalidStateSteppingException {
		checkSteppingValidity(steps, direction);
		
		switch(direction) {
		case FORWARD:
			statePointer += steps;
		case BACKWARD:
			statePointer -= steps;
		}
	}
	
	private enum STEPPING_DIRECTION {
		FORWARD,
		BACKWARD
	}

}
