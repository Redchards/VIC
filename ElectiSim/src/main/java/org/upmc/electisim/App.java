package org.upmc.electisim;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
    	int committeeSize = 3;
    	IPreferencesGenerator gen = new RandomPreferencesGenerator();
    	
    	List<Candidate> cl = Arrays.asList(new Candidate[] {
    			new Candidate("A"),
    			new Candidate("B"),
    			new Candidate("C"),
    			new Candidate("D"),
    			new Candidate("E")
    	});
    	
    	List<Agent> al = new ArrayList<>();
    	
    	for(int i = 0; i < 100; i++) {
    		al.add(new Agent("a" + Integer.toString(i), 
    				gen.generate(cl, PreferenceType.RESPONSIVE, committeeSize)));
    	}
    	
    	//SimulationEngine engine = new SimulationEngine()
    }
}