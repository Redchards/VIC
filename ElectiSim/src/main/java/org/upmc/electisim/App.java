package org.upmc.electisim;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.upmc.electisim.output.InvalidStateException;

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
    	
    	/*for(int i = 0; i < 100; i++) {
    		al.add(new Agent("a" + Integer.toString(i), 
    				gen.generate(cl, PreferenceType.RESPONSIVE, committeeSize)));
    	}*/
    	
    	al.add(new Agent("a1", new Preferences(PreferenceType.RESPONSIVE, new ArrayList<Candidate>(cl))));
    	al.add(new Agent("a2", new Preferences(PreferenceType.RESPONSIVE, new ArrayList<Candidate>(cl))));
    	List<Candidate> tst = new ArrayList<>(cl);
    	tst.set(4, cl.get(0));
    	tst.set(0, cl.get(4));
    	al.add(new Agent("a3", new Preferences(PreferenceType.RESPONSIVE, tst)));

    	
    	IVotingRule rule = new BlocVotingRule();
    	SimulationProfile profile = new SimulationProfile(PreferenceType.RESPONSIVE, rule, new OmniscientBestResponseStrategy(), al, cl);
    	
    	SimulationEngine engine = new SimulationEngine(profile, committeeSize);
    	engine.run();
    	
    	System.out.println(al.get(0).getPreferences().getPreferenceList());
    	System.out.println(al.get(1).getPreferences().getPreferenceList());
    	System.out.println(al.get(2).getPreferences().getPreferenceList());

    	try {
			engine.saveCurrentState("test");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}