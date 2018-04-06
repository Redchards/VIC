package org.upmc.electisim;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World! " );
        CircularBuffer<Integer> cbuf = new CircularBuffer<Integer>(5, 5);
        
        for(int i=0; i<4; i++)
        {
        	cbuf.push(i);
        	cbuf.printBuffer();
        }
        System.out.println(cbuf.getLast());
        cbuf.remove(3);
        System.out.println("after removing 3rd elt : ");
        cbuf.printBuffer();
        System.out.println(cbuf.getLast());
        cbuf.push(12);
        cbuf.printBuffer();
        System.out.println(cbuf.getLast());
        cbuf.push(13);
        cbuf.printBuffer();
        System.out.println(cbuf.getLast());
        System.out.println(cbuf.pop());
        cbuf.printBuffer();
        cbuf.getLast();
        cbuf.push(20);
        cbuf.printBuffer();
        System.out.println(cbuf.pop());
        cbuf.printBuffer();
    }
}
