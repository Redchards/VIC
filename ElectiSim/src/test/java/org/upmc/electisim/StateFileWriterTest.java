package org.upmc.electisim;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.upmc.electisim.output.StateFileWriter;

public class StateFileWriterTest {

	@Test
	public void test() {
		try {
			StateFileWriter sfw = new StateFileWriter("test.csv");
			SimulationProfile profile = new SimulationProfile(null, null, null, null, null);
			
			Candidate c1 = new Candidate("c1");
			Candidate c2 = new Candidate("c2");
			
			Map<Candidate, Integer> scores1 = new HashMap<Candidate, Integer>();
			scores1.put(c1, 0);
			scores1.put(c2, 10);
			VoteResult voteResult1 = new VoteResult(new Agent("A", null), scores1);		
			
			Map<Candidate, Integer> scores2 = new HashMap<Candidate, Integer>();
			scores2.put(c2, 3);
			scores2.put(c1, 7);
			
			VoteResult voteResult2 = new VoteResult(new Agent("B", null), scores2);
			
			List<VoteResult> results = new ArrayList<>();
			results.add(voteResult1);
			results.add(voteResult2);
			
			SimulationState state = new SimulationState(profile, results);
			
			sfw.writeState(state);
			
			
			sfw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}
