package org.upmc.electisim;

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
    	int committeeSize = 2;
    	IPreferencesGenerator gen = new RandomPreferencesGenerator();
    	
    	List<IElectable> cl = Arrays.asList(new IElectable[] {
    			new	Candidate("A"),
    			new Candidate("D"),
    			new Candidate("C"),
    			new Candidate("B"),
/*    			new Candidate("E"),
    			new Candidate("F"),
    			new Candidate("G"),
    			new Candidate("H"),
    			new Candidate("I"),
    			new Candidate("J")
*/
    	});
    	
    	List<Agent> al = new ArrayList<>();
    	
    	/*
    	for(int i = 0; i < 100; i++) {
    		al.add(new Agent("a" + Integer.toString(i), 
    				gen.generate(cl, PreferenceType.RESPONSIVE, committeeSize)));
    	}
    	*/
    	al.add(new Agent("a1", new Preferences(PreferenceType.RESPONSIVE, new ArrayList<IElectable>(cl))));
    	
    	
    	List<IElectable> tst = new ArrayList<>(cl);
/*    	tst.set(3, cl.get(0));
    	tst.set(2, cl.get(1));
    	tst.set(1, cl.get(2));
    	tst.set(0, cl.get(3));
    	//al.add(new Agent("a1", new Preferences(PreferenceType.RESPONSIVE, tst)));
*/
    	al.add(new Agent("a2", new Preferences(PreferenceType.RESPONSIVE, tst)));
/*
    	List<IElectable> tst2 = new ArrayList<>(cl);
    	tst.set(2, cl.get(0));
    	tst.set(0, cl.get(1));
    	tst.set(3, cl.get(2));
    	tst.set(1, cl.get(3));
    	
    	al.add(new Agent("a3", new Preferences(PreferenceType.RESPONSIVE, tst2)));

  */  	
    	//IVotingRule rule = new BlocVotingRule();
    	IVotingRule rule = new BordaVotingRule();
    	SimulationProfile profile = new SimulationProfile(PreferenceType.RESPONSIVE, rule, new OmniscientBestResponseStrategy(), al, cl);
    	
    	SimulationEngine engine = new SimulationEngine(profile, committeeSize, 10);
    	
    	//engine.step();
    	
    	/*try {
			//engine.run();
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
    	
    	
    	/*
    	try(SimulationSaveFileWriter ssfw = new SimulationSaveFileWriter("profile_save3");)
    			//SimulationSaveFileReader ssfr = new SimulationSaveFileReader("profile_save.json");
    			//SimulationSaveFileWriter ssfw2 = new SimulationSaveFileWriter("profile_save2");)
    	{


    		ssfw.writeProfile(profile);

    		//SimulationProfile profile2 = ssfr.loadProfile();
    		//ssfw2.writeProfile(profile2);
    		
    	} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidExtensionException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	*/
    }
}