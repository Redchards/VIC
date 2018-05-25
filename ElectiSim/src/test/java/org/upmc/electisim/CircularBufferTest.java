package org.upmc.electisim;

import java.util.ArrayList;

import org.junit.Test;
import org.upmc.electisim.utils.CircularBuffer;
import org.upmc.electisim.utils.EmptyBufferException;

public class CircularBufferTest {

	CircularBuffer<Integer> cbuf = new CircularBuffer<Integer>(5);
	
	
	@Test
	public void testCircularBufferIntT() {
		System.out.println("*****************Constructor with size param test*****************");
		CircularBuffer<Integer> cbufInt = new CircularBuffer<Integer>(5);
		
		for(int i=0; i<7; i++)
		{
			cbufInt.push(i);
		}
		cbufInt.printBuffer();
	}

	@Test
	public void testCircularBufferCollectionOfT() throws EmptyBufferException {
		System.out.println("*****************Constructor with Collection param test*****************");
		ArrayList<String> stringCol = new ArrayList<String>(5);
		stringCol.add("January");
		stringCol.add("February");
		
		CircularBuffer<String> cbuf = new CircularBuffer<>(stringCol);
		cbuf.printBuffer();
		System.out.println("Size : "+cbuf.getSize());
		cbuf.push("March");
		System.out.print("Buffer state after pushing \"March\" : ");
		cbuf.printBuffer();
		
		System.out.println("Element popped : "+cbuf.pop());
		System.out.print("Buffer state after pushing \"March\" : ");
		cbuf.printBuffer();

	}
	 
	
	@Test
	public void testPush() {
		
		System.out.println("*****************Push() test*****************");
		for(int i=0; i<8; i++)
        {
        	cbuf.push(i);
        	cbuf.printBuffer();
        }
		
		
	}

	@Test
	public void testGetLast() throws EmptyBufferException {
		System.out.println("*****************GetLast() test*****************");
		for(int i=0; i<11; i++)
        {
        	cbuf.push(i);
        	cbuf.printBuffer();
        }
		
		System.out.println("Last element is : "+cbuf.getLast());
	}

	@Test
	public void testPop() throws EmptyBufferException {
		System.out.println("*****************Pop() test*****************");
		for(int i=0; i<8; i++)
        {
        	cbuf.push(i);
        	cbuf.printBuffer();
        }
		System.out.println("Element popped : "+cbuf.pop());
		System.out.println("Buffer after pop() : ");
		cbuf.printBuffer();
	}

	@Test
	public void testReplace() {
		System.out.println("*****************Replace() test*****************");
		for(int i=0; i<8; i++)
        {
        	cbuf.push(i);
        	cbuf.printBuffer();
        }
		int index = 0;
		int newVal = 6;
		cbuf.replace(index, newVal);
		System.out.println("Buffer state after replacing element at index "+index+" with value "+newVal);
		cbuf.printBuffer();
		cbuf.push(2);
		System.out.println("Buffer state after pushing 2");
		cbuf.printBuffer();
	}

	@Test
	public void testRemove() {
		System.out.println("*****************Remove() test*****************");
		for(int i=0; i<11; i++)
        {
        	cbuf.push(i);
        	cbuf.printBuffer();
        }
		int index = 3;
		cbuf.remove(3);
		System.out.println("after removing elt at index : "+index);
		cbuf.printBuffer();
	}

	@Test
	public void testGetSize() {
		System.out.println("*****************GetSize() test*****************");
		cbuf.push(245);
		cbuf.push(6);
		cbuf.printBuffer();
		System.out.println("Buffer size is : "+cbuf.getSize());
		
		for(int i=10; i<17; i++)
		{
			cbuf.push(i);
		}
		System.out.print("New buffer state : ");
		cbuf.printBuffer();
		System.out.println("size : "+cbuf.getSize());
		
	}

		
	@Test
	public void testAll() throws EmptyBufferException{
		
		System.out.println("*****************Cocktail test*****************");
		for(int i=0; i<10; i++)
        {
        	cbuf.push(i);
        	cbuf.printBuffer();
        }
		
		 System.out.println("Last element is : "+cbuf.getLast());
		 
		 int index = 3;
	     cbuf.remove(3);
	     System.out.println("after removing element at index : "+index);
	     cbuf.printBuffer();
	     System.out.println("Last element of the buffer : "+cbuf.getLast());
	     
	     cbuf.push(12);
	     System.out.print("Buffer state after pushing 12 : ");
	     cbuf.printBuffer();
	     System.out.println("Last element of the buffer : "+cbuf.getLast());
	     
	     cbuf.push(13);
	     System.out.print("Buffer state after pushing 13 :");
	     cbuf.printBuffer();
	     
	     System.out.println("Element popped : "+cbuf.pop());
	     System.out.print("Buffer state after popping last element : ");
	     cbuf.printBuffer();
	     System.out.println("Last element of the buffer : "+cbuf.getLast());
	     
	     cbuf.push(20);
	     System.out.print("Buffer state after pushing 20 : ");
	     cbuf.printBuffer();
	     
	     System.out.println("Element popped : "+cbuf.pop());
	     System.out.println("Buffer state after popping last element : ");
	     cbuf.printBuffer();
	     
	}

}
