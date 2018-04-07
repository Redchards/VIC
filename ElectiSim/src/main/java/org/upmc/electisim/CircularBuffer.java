package org.upmc.electisim;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CircularBuffer<T> {
	
	private List<T> buffer;
	private int bufferCapacity;
	//private int currentPointer;
	private int writeIndex = 0; //where the data is put -> points at the newest data
	private int readIndex = 0; //where the data is read -> points at the oldest data
	
	
	public CircularBuffer(int bufferSize, T obj){
		bufferCapacity = bufferSize;
		buffer = new ArrayList<T>(bufferCapacity);
		//currentPointer = 0;
		
	}
	
	public CircularBuffer(Collection<T> col){
		buffer = new ArrayList<>(col);
		bufferCapacity = col.size();
		//currentPointer = 0;
	}
	
	public void push(T obj){
		
		
		if(buffer.size()<bufferCapacity) //buffer not full
		{
			buffer.add(writeIndex, obj);
			if(writeIndex<=readIndex)
				readIndex = wrapIndex(readIndex+1);
		}
		else //buffer full, we must replace the oldest element added
		{
			buffer.set(writeIndex, obj);
			if(writeIndex==readIndex)
				readIndex = wrapIndex(readIndex+1);

		}


		writeIndex = wrapIndex(writeIndex+1);
				
	}
	
	public T getLast(){
		return buffer.get(readIndex);
	}
	
	public T pop(){
		int popIndex = readIndex;
		return buffer.remove(popIndex);
		
	}
	
	
	public void replace(int index, T obj){
		if(buffer.size()<index)
			throw new ArrayIndexOutOfBoundsException();
		else
			buffer.set(index, obj);
	}
	
	public void remove(int index){
		if(buffer.size()<index)
			throw new ArrayIndexOutOfBoundsException();
		else
		{
			buffer.remove(index);
			if(index < readIndex)
				readIndex = wrapIndex(readIndex-1);
			if(index < writeIndex)
				writeIndex = wrapIndex(writeIndex-1);
		}
	}
	
	public int getSize(){
		return buffer.size();
	}
	
	public void printBuffer(){
		System.out.println(buffer.toString());
	}
	
	
	private int wrapIndex(int index) {
		
		return index % bufferCapacity;
		
    }

}

