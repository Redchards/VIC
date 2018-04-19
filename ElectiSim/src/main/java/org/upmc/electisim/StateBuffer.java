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
	
	public void clearBuffer() {
		super.clearBuffer();
		statePointer = 0;
	}
	
	private void checkSteppingValidity(int steps, STEPPING_DIRECTION direction) throws InvalidStateSteppingException {
		if(steps > getCapacity()) {
			throw new InvalidStateSteppingException(steps, direction.toString().toLowerCase());
		}
		
		int dist = currentPointerToCurrentStateDistance(direction);
		
		if(dist > steps) {
			throw new InvalidStateSteppingException(steps, direction.toString().toLowerCase());
		}
	}
	
	// TODO : maybe make this public along with the STEPPING_DIRECTION enum ?
	private void step(int steps, STEPPING_DIRECTION direction) throws InvalidStateSteppingException {
		checkSteppingValidity(steps, direction);
		
		switch(direction) {
		case FORWARD:
			statePointer += steps;
			break;
		case BACKWARD:
			statePointer -= steps;
			break;
		}
	}
	
	private int currentPointerToCurrentStateDistance(STEPPING_DIRECTION direction) {
		switch(direction) {
		case FORWARD:
			if(statePointer > currentPointer) {
				return (getCapacity() - statePointer) + (currentPointer - 1);
			}
			else {
				return (currentPointer - 1) - statePointer;
			}
		case BACKWARD:
			if(statePointer > currentPointer) {
				return statePointer - (currentPointer - 1);
			}
			else {
				return statePointer + (getCapacity() - (currentPointer - 1));
			}
		default:
			return 0;
		}
	}
	
	private enum STEPPING_DIRECTION {
		FORWARD,
		BACKWARD
	}

}
