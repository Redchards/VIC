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
    			new Candidate("B"),
    			new Candidate("C"),
    			new Candidate("D"),
//    			new Candidate("E"),
//    			new Candidate("F"),
//    			new Candidate("G"),
//    			new Candidate("H"),
//    			new Candidate("I"),
//    			new Candidate("J")

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
    	tst.set(3, cl.get(0));
    	tst.set(2, cl.get(1));
    	tst.set(1, cl.get(2));
    	tst.set(0, cl.get(3));
    	//al.add(new Agent("a1", new Preferences(PreferenceType.RESPONSIVE, tst)));

    	al.add(new Agent("a2", new Preferences(PreferenceType.RESPONSIVE, tst)));

    	
//    	List<IElectable> tst2 = new ArrayList<>(cl);
//    	tst2.set(5, cl.get(0));
//    	tst2.set(0, cl.get(5));
//    	tst2.set(0, cl.get(2));
//    	tst2.set(1, cl.get(3));
    	
//    	al.add(new Agent("a3", new Preferences(PreferenceType.RESPONSIVE, tst2)));

    	
    	//IVotingRule rule = new BlocVotingRule();
//    	IVotingRule rule = new BordaVotingRule();
//    	IVotingRule rule = new ChamberlinCourantVotingRule(cl, committeeSize);
    	IVotingRule rule = new ChamberlinCourantVotingRule();
    	SimulationProfile profile = new SimulationProfile(PreferenceType.RESPONSIVE, rule, new OmniscientBestResponseStrategy(), al, cl);
    	
    	SimulationEngine engine = new SimulationEngine(profile, committeeSize, 10);
    	/*
    	engine.step();
    	engine.step();
    	engine.step();
    	*/
//    	try {
//			engine.run();
//		} catch (InterruptedException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
//
    	Application.launch(gui.upmc.electisim.App.class, args);
    	
    	/*for(Agent a : al) {
    		System.out.println(a.getPreferences().getPreferenceList());
    	}*/
    	
//    	try {
//			engine.saveCurrentState("testcc");
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (InvalidExtensionException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//    	
    	
    	
//    	try(SimulationSaveFileWriter ssfw = new SimulationSaveFileWriter("tests\\3 - comité 2\\profile");)
//    			//SimulationSaveFileReader ssfr = new SimulationSaveFileReader("profile_save.json");
//    			//SimulationSaveFileWriter ssfw2 = new SimulationSaveFileWriter("profile_save2");)
//    	{
//
//
//    		ssfw.writeProfile(profile);
//
//    		//SimulationProfile profile2 = ssfr.loadProfile();
//    		//ssfw2.writeProfile(profile2);
//    		
//    	} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (InvalidExtensionException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		} 
//    	
    }
}