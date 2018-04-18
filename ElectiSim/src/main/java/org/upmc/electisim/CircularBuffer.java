package org.upmc.electisim;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CircularBuffer<T> {
	
	private List<T> buffer;
	private int bufferCapacity;
	private int currentPointer = 0;
	private int currentSize;
	//private int writeIndex = 0; //where the data is put -> points at the newest data
	//private int readIndex = 0; //where the data is read -> points at the oldest data
	
	
	public CircularBuffer(int bufferSize){
		buffer = new ArrayList<T>(bufferCapacity);
		for(int i = 0; i < bufferSize; i++) {
			buffer.add((T) new Object());
		}
		
		bufferCapacity = bufferSize;
		currentSize = 0;
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
		return buffer.size();
	}
	
	public void printBuffer(){
		System.out.println(buffer.toString());
	}
	
	
	private int wrapIndex(int index) {		
		if(index < 0) {
			return bufferCapacity - 1;
		}
		
		return index % bufferCapacity;
    }
	
	private boolean validIndex(int index) {
		return (index >= 0 && index < bufferCapacity);
	}
	
	private void shiftLeft(int begIdx, int endIdx) {
		for(int i = begIdx; i < endIdx - 1; i++) {
			buffer.set(i, buffer.get(i + 1));
		}
	}
	
	private void shiftRight(int begIdx, int endIdx) {
		for(int i = endIdx; i > begIdx + 1; i--) {
			buffer.set(i, buffer.get(i - 1));
		}
	}

}

