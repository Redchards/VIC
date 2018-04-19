package org.upmc.electisim;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.upmc.electisim.output.SimulationProfileFileWriter;

public class SimulationProfileWriterTest {

	@Test
	public void test() {
		PreferenceType type = PreferenceType.HAMMING;
		IVotingRule rule = new BlocVotingRule() ;
		IAgentStrategy strategy = new OmniscientBestResponseStrategy(null, rule);
		
		Candidate c1 = new Candidate("c1");
		Candidate c2 = new Candidate("c2");
		List<Candidate> candidateList = new ArrayList<Candidate>();
		candidateList.add(c1);
		candidateList.add(c2);
		
		
		List<Candidate> prefListA = new ArrayList<>();
		prefListA.add(c2);
		Agent a1 = new Agent("A", new Preferences(null, null, prefListA, null));
		Agent a2 = new Agent("B", new Preferences(null, null, candidateList, null));
		List<Agent> agentList = new ArrayList<Agent>();
		agentList.add(a1);
		agentList.add(a2);
		
		SimulationProfile profile = new SimulationProfile(type, rule, strategy, agentList, candidateList);
		
		
		try {
			SimulationProfileFileWriter profileWriter = new SimulationProfileFileWriter("test_profile.csv");
			profileWriter.writeProfile(profile);
			profileWriter.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}
