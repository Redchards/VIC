package org.upmc.electisim.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.upmc.electisim.StateBuffer;

/**
 * <p>A general purpose implementation of the circular buffer data structure.
 * The structure has a capacity, and if this capacity is reached, it wraps and erases
 * the elements found.</p>
 * <p>This data structure is used as the underlying structure of the 
 * {@link org.upmc.electisim.StateBuffer} used to keep track the states of the simulation.</p>
 * 
 * @param <T> The type contained in the data structure
 */
public class CircularBuffer<T> {
	
	/*
	 * (non-javadoc)
	 * The underlying buffer
	 */
	protected List<T> buffer;
	
	/*
	 * (non-javadoc)
	 * The buffer capacity
	 */
	private int bufferCapacity;
	
	/*
	 * (non-javadoc)
	 * The current pointer in the buffer
	 */
	protected int currentPointer = 0;
	
	/*
	 * (non-javadoc)
	 * The current size of the buffer
	 */
	private int currentSize;
	
	/**
	 * Builds a circular buffer from a capacity
	 * @param bufferCapacity the buffer capacity
	 */
	public CircularBuffer(int bufferCapacity){
		initEmptyBuffer(bufferCapacity);
		bufferCapacity = bufferCapacity;
	}
	
	/**
	 * Builds a circular buffer from another circular buffer. Acts as a copy constructor
	 * @param other the other buffer from which to copy the data
	 */
	public CircularBuffer(CircularBuffer<T> other) {
		this.buffer = new ArrayList<>(other.buffer);
		this.bufferCapacity = other.bufferCapacity;
		this.currentPointer = other.currentPointer;
		this.currentSize = other.currentSize;
	}
	
	/**
	 * Builds a circular buffer from a generic collection
	 * @param col the collection from which to copy the data
	 */
	public CircularBuffer(Collection<T> col){
		buffer = new ArrayList<>(col);
		bufferCapacity = col.size();
		currentSize = bufferCapacity;
	}
	
	/**
	 * Push an element in the buffer
	 * @param obj the object to push on the buffer
	 */
	public void push(T obj){
		buffer.set(currentPointer, obj);
		currentPointer = wrapIndex(currentPointer + 1);
		
		if(currentSize != bufferCapacity) {
			currentSize++;
		}
	}
	
	/**
	 * Retrieves the element at the given index
	 * 
	 * @param index the index of the element to retrieve
	 * @return The element at the index given
	 * @throws IndexOutOfBoundsException
	 */
	public T get(int index) throws IndexOutOfBoundsException {
		if(index >= currentSize || index >= bufferCapacity) {
			throw new IndexOutOfBoundsException();
		}
		return buffer.get(index);
	}
	
	/**
	 * @return The last element in the buffer
	 * @throws EmptyBufferException 
	 */
	public T getLast() throws EmptyBufferException{
		if(currentSize == 0) {
			throw new EmptyBufferException();
		}
		return buffer.get(wrapIndex(currentPointer - 1));
	}
	
	/**
	 * Deletes the last element of the buffer and returns it
	 * 
	 * @return The last element of the buffer
	 * @throws EmptyBufferException
	 */
	public T pop() throws EmptyBufferException{
		if(currentSize == 0) {
			throw new EmptyBufferException();
		}
		
		T last = getLast();

		currentSize--;
		currentPointer = wrapIndex(currentPointer - 1);
		return last;
		
	}
	
	/**
	 * Replace the element at the given index
	 * 
	 * @param index the index of the element to be replaced
	 * @param obj the new object to replace the original element with
	 * @throws IndexOutOfBoundsException
	 */
	public void replace(int index, T obj) {
		if(!validIndex(index)) {
			throw new IndexOutOfBoundsException();
		}
		buffer.set(index, obj);
	}
	
	/**
	 * Remove an element at a given index and ensure that the underlying buffer is contiguous.
	 * Warning : this operation is potentially expensive, being O(n)
	 * 
	 * @param index the index of the element to be removed
	 * @throws IndexOutOfBoundsException
	 */
	public void remove(int index){
		if(!validIndex(index)) {
			throw new IndexOutOfBoundsException();
		}
		buffer.set(index, null);
		if(index < currentPointer) {
			this.shiftLeft(index, currentPointer - 1);
		}
		else if(index > currentPointer) {
			this.shiftRight(currentPointer, index);
		}
		
		currentSize--;
	}
	
	/**
	 * @return The current size of the buffer
	 */
	public int getSize(){
		return currentSize;
	}
	
	/**
	 * @return The capacity of the buffer
	 */
	public int getCapacity() {
		return bufferCapacity;
	}
	
	// TODO : REMOVE THIS !! FIND A BETTER WAY TO WRITE THE CSVS !!!
	public int getCurrentPointer() {
		return currentPointer;
	}
	
	public void clearBuffer() {
		initEmptyBuffer(bufferCapacity);
	}
	
	public void printBuffer(){
		System.out.println(buffer.toString());
	}
	
	
	protected int wrapIndex(int index) {		
		if(index < 0) {
			return bufferCapacity + index;
		}
		
		return index % bufferCapacity;
    }
	
	protected boolean validIndex(int index) {
		return (index >= 0 && index < bufferCapacity);
	}
	
	protected void shiftLeft(int begIdx, int endIdx) {
		for(int i = begIdx; i < endIdx ; i++) {
			buffer.set(i, buffer.get(i + 1));
		}
	}
	
	protected void shiftRight(int begIdx, int endIdx) {
		for(int i = endIdx; i > begIdx ; i--) {
			buffer.set(i, buffer.get(i - 1));
		}
	}
	
	private void initEmptyBuffer(int bufferSize) {
		buffer = new ArrayList<>(bufferCapacity);
		for(int i = 0; i < bufferSize; i++) {
			buffer.add(null);
		}
		currentPointer = 0;
		currentSize = 0;
	}

}

