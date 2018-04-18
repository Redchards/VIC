package org.upmc.electisim;

import java.io.FileNotFoundException;

import org.junit.Test;
import org.upmc.electisim.output.StateFileWriter;

public class StateFileWriterTest {

	@Test
	public void test() {
		try {
			StateFileWriter sfw = new StateFileWriter("test.csv");
			
			sfw.writeState(new SimulationState(null, null));
			
			sfw.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
	}

}
