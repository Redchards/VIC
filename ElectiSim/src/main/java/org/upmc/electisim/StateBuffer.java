package org.upmc.electisim;

import org.upmc.electisim.utils.CircularBuffer;
import org.upmc.electisim.utils.EmptyBufferException;

/**
 * The buffer containing all the states of the simulation. All the stepping operations
 * are performed on this data structure. Is essentially similar to a circular buffer with
 * few stepping operations on top.
 * 
 * NOTE : It should be noted that this class is very powerful and holds much power on the simulation
 * process, hence why it should be only seldomly exposed to the user, if ever.
 */
public class StateBuffer extends CircularBuffer<SimulationState> {
		
	/*
	 * (non-Javadoc)
	 * The default buffer capacity
	 */
	private static final int DEFAULT_BUFFER_CAPACITY = 100;
	
	/*
	 * (non-Javadoc)
	 * The state pointer
	 */
	private int statePointer = 0;


	/**
	 * Build a state buffer using the default arguments
	 */
	public StateBuffer(){
		super(DEFAULT_BUFFER_CAPACITY);
	}

	/**
	 * Build a state buffer using a buffer capacity
	 * 
	 * @param bufCapacity the buffer capacity
	 */
	public StateBuffer(int bufCapacity) {
		super(bufCapacity);
	}
	
	/**
	 * Build a state buffer by copying another buffer
	 * 
	 * @param other the buffer to copy
	 */
	public StateBuffer(StateBuffer other) {
		super(other);
		this.statePointer = other.statePointer;
	}
	
	/**
	 * @return The current simulation state, if it exists
	 */
	public SimulationState getCurrent() {
		return this.get(this.wrapIndex(statePointer - 1));
	}
	
	/**
	 * @return The previous simulation state, if it exists
	 */
	public SimulationState getPrevious() {
		return this.get(this.wrapIndex(statePointer - 2));
	}
	
	/*
	 * (non-Javadoc)
	 * Push an element into the buffer
	 * 
	 * @see org.upmc.electisim.utils.CircularBuffer#push(java.lang.Object)
	 */
	@Override
	public void push(SimulationState state) {
		statePointer = this.wrapIndex(statePointer + 1);
		super.push(state);
	}
	
	/*
	 * (non-Javadoc)
	 * Pop the last element
	 * 
	 * @see org.upmc.electisim.utils.CircularBuffer#pop()
	 */
	@Override
	public SimulationState pop() throws EmptyBufferException {
		statePointer = this.wrapIndex(statePointer - 1);
		return super.pop();
	}
	
	/*
	 * (non-Javadoc)
	 * Remove the element at the given index
	 * 
	 * @see org.upmc.electisim.utils.CircularBuffer#remove(int)
	 */
	@Override
	public void remove(int idx) {
		statePointer = this.wrapIndex(statePointer - 1);
		super.remove(idx);
	}
	
	/**
	 * Rewind a certain number of steps
	 * 
	 * @param steps the number of steps to rewind
	 * @throws InvalidStateSteppingException if the stepping operation is invalid
	 */
	public void rewind(int steps) throws InvalidStateSteppingException {
		step(steps, SteppingDirection.BACKWARD);
	}
	
	/**
	 * Rewind exactly one step
	 * 
	 * @throws InvalidStateSteppingException if the stepping operation is invalid
	 */
	public void rewindStep() throws InvalidStateSteppingException {
		this.rewind(1);
	}
	
	/**
	 * Advance a certain number of steps
	 * 
	 * @param steps the number of steps to advance
	 * @throws InvalidStateSteppingException if the stepping operation is invalid
	 */
	public void advance(int steps) throws InvalidStateSteppingException {
		step(steps, SteppingDirection.FORWARD);
	}
	
	/**
	 * Advance exactly on step
	 * 
	 * @throws InvalidStateSteppingException if the stepping operation is invalid
	 */
	public void advanceStep() throws InvalidStateSteppingException {
		this.advance(1);
	}
	
	/*
	 * (non-Javadoc)
	 * Clear the buffer
	 * 
	 * @see org.upmc.electisim.utils.CircularBuffer#clearBuffer()
	 */
	@Override
	public void clearBuffer() {
		super.clearBuffer();
		statePointer = 0;
	}
	
	/*
	 * (non-Javadoc)
	 * Check the validity of the stepping operation
	 * 
	 * @param steps the number of steps to take in the given direction
	 * @param direction the direction in which to step
	 */
	private void checkSteppingValidity(int steps, SteppingDirection direction) throws InvalidStateSteppingException {
		if(steps > getCapacity()) {
			throw new InvalidStateSteppingException(steps, direction.toString().toLowerCase());
		}
		
		int dist = currentPointerToCurrentStateDistance(direction);
		
		if(dist > steps) {
			throw new InvalidStateSteppingException(steps, direction.toString().toLowerCase());
		}
	}
	
	// TODO : maybe make this public along with the STEPPING_DIRECTION enum ?
	/*
	 * (non-Javadoc)
	 * Step in a certain direction
	 * 
	 * @param steps the number of steps to take in the given direction
	 * @param direction the direction in which to step
	 */
	private void step(int steps, SteppingDirection direction) throws InvalidStateSteppingException {
		checkSteppingValidity(steps, direction);
		
		switch(direction) {
		case FORWARD:
			statePointer = this.wrapIndex(statePointer + steps);
			break;
		case BACKWARD:
			statePointer = this.wrapIndex(statePointer - steps);
			break;
		}
	}
	
	/*
	 * (non-Javadoc)
	 * The distance between the last state and the current pointer in a given direction
	 * 
	 * @param direction the direction to consider
	 */
	private int currentPointerToCurrentStateDistance(SteppingDirection direction) {
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
	
	/*
	 * (non-Javadoc)
	 * An enumeration representing the stepping direction
	 */
	private enum SteppingDirection {
		FORWARD,
		BACKWARD
	}

}
