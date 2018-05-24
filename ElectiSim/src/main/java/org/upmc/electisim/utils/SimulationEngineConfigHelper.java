package org.upmc.electisim.utils;

public class SimulationEngineConfigHelper {
	private static final int DEFAULT_TIMESTEP = 36;
	private static final int DEFAULT_BUFFER_SIZE = 100;
	private static final int DEFAULT_STEP_COUNT = 0;
	
	public static int getDefaultTimestep() {
		return DEFAULT_TIMESTEP;
	}
	
	public static int getDefaultBufferSize() {
		return DEFAULT_BUFFER_SIZE;
	}
	
	public static int getDefaultStepCount() {
		return DEFAULT_STEP_COUNT;
	}
}
