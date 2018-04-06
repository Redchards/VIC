package org.upmc.electisim;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CircularBuffer<T> {
	
	private List<T> buffer;
	private int bufferCapacity;
	private int currentPointer;
	private int writeIndex = 0; //where the data is put -> points at the newest data
	private int readIndex = 0; //where the data is read -> points at the oldest data
	
	
	public CircularBuffer(int bufferSize, T obj){
		bufferCapacity = bufferSize;
		buffer = new ArrayList<T>(bufferCapacity);
		currentPointer = 0;
		
	}
	
	public CircularBuffer(Collection<T> col){
		buffer = new ArrayList<>(col);
		bufferCapacity = buffer.size();
		currentPointer = 0;
	}
	
	public void push(T obj){
		
		
		if(writeIndex>buffer.size()) //buffer not full
		{
			buffer.add(obj);
		}
		else
		{
			if(buffer.size()<bufferCapacity) //an element has been removed
			{
				buffer.add(writeIndex, obj);
				System.out.println("writeindx="+writeIndex+" and readindx="+readIndex);
				if(writeIndex<=readIndex)
					readIndex = wrapIndex(readIndex+1);
			}
			else //buffer full, we must replace the oldest element added
			{
				buffer.set(writeIndex, obj);
				if(writeIndex==readIndex)
					readIndex = wrapIndex(readIndex+1);
			}
		}
		
		writeIndex = wrapIndex(writeIndex+1);
		//System.out.println("new list : "+buffer.toString());		
	}
	
	public T getLast(){
		return buffer.get(readIndex);
	}
	
	public T pop(){
		int popIndex = readIndex;
		readIndex = wrapIndex(readIndex+1);
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
		int newIndex = 0;
		if(buffer.size()==0)
			newIndex = index % bufferCapacity; //buffer.size();
		else
			newIndex = index % buffer.size();
        /*
        if (newIndex < 0) { 
         
            newIndex += bufferCapacity;
        }
        */
        return newIndex;
    }

}
