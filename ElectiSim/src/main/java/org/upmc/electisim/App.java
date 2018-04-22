package org.upmc.electisim;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javafx.application.Application;


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
    			new Candidate("E"),
    			/*new Candidate("F"),
    			new Candidate("G"),
    			new Candidate("H"),
    			new Candidate("I"),
    			new Candidate("J")*/

    	});
    	
    	List<Agent> al = new ArrayList<>();
    	
    	for(int i = 0; i < 100; i++) {
    		al.add(new Agent("a" + Integer.toString(i), 
    				gen.generate(cl, PreferenceType.RESPONSIVE, committeeSize)));
    	}
    	
    	//al.add(new Agent("a1", new Preferences(PreferenceType.RESPONSIVE, new ArrayList<Candidate>(cl))));
    	/*List<Candidate> tst = new ArrayList<>(cl);
    	tst.set(4, cl.get(0));
    	tst.set(0, cl.get(4));
    	al.add(new Agent("a1", new Preferences(PreferenceType.RESPONSIVE, tst)));

    	al.add(new Agent("a2", new Preferences(PreferenceType.RESPONSIVE, tst)));

    	al.add(new Agent("a3", new Preferences(PreferenceType.RESPONSIVE, tst)));*/

    	
    	IVotingRule rule = new BlocVotingRule();
    	SimulationProfile profile = new SimulationProfile(PreferenceType.RESPONSIVE, rule, new OmniscientBestResponseStrategy(), al, cl);
    	
    	/*SimulationEngine engine = new SimulationEngine(profile, committeeSize, 10);
    	try {
			engine.run();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}*/
    	
    	Application.launch(gui.upmc.electisim.App.class, args);
    	
    	/*for(Agent a : al) {
    		System.out.println(a.getPreferences().getPreferenceList());
    	}*/
    	
    	/*try {
			engine.saveCurrentState("test");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
    }
}