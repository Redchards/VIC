package org.upmc.electisim;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CircularBuffer<T> {
	
	protected List<T> buffer;
	private int bufferCapacity;
	protected int currentPointer = 0;
	private int currentSize;
	//private int writeIndex = 0; //where the data is put -> points at the newest data
	//private int readIndex = 0; //where the data is read -> points at the oldest data
	
	
	public CircularBuffer(int bufferSize){
		initEmptyBuffer(bufferSize);
		bufferCapacity = bufferSize;
	}
	
	public CircularBuffer(CircularBuffer<T> other) {
		this.buffer = new ArrayList<>(other.buffer);
		this.bufferCapacity = other.bufferCapacity;
		this.currentPointer = other.currentPointer;
		this.currentSize = other.currentSize;
	}
	
	public CircularBuffer(Collection<T> col){
		buffer = new ArrayList<>(col);
		bufferCapacity = col.size();
		currentSize = bufferCapacity;
	}
	
	public void push(T obj){
		buffer.set(currentPointer, obj);
		currentPointer = wrapIndex(currentPointer + 1);
		
		if(currentSize != bufferCapacity) {
			currentSize++;
		}
	}
	
	// Handle the case where we went beyond the current buffer size !
	public T get(int index) {
		return buffer.get(index);
	}
	
	public T getLast(){
		return buffer.get(wrapIndex(currentPointer - 1));
	}
	
	public T pop() throws EmptyBufferException{
		if(currentSize == 0) {
			throw new EmptyBufferException();
		}
		
		T last = getLast();

		currentSize--;
		currentPointer = wrapIndex(currentPointer - 1);
		return last;
		
	}
	
	
	public void replace(int index, T obj){
		if(!validIndex(index)) {
			throw new ArrayIndexOutOfBoundsException();
		}
		buffer.set(index, obj);
	}
	
	public void remove(int index){
		if(!validIndex(index)) {
			throw new ArrayIndexOutOfBoundsException();
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
	
	public int getSize(){
		return currentSize;
	}
	
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

