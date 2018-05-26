package org.upmc.electisim;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.EventListener;
import java.util.List;

import org.upmc.electisim.SimulationEngine.ResultListener;

public class CycleDetector {
	private List<HashValue> hashBuffer;
	private List<CycleInfo> detectedCycles;
	
	private IHashProvider hashProvider;
	
	public interface CycleDetectedListener extends EventListener {
		public void cycleDetected(CycleInfo info);
	}
	
	private List<CycleDetectedListener> cycleDetectionListenerList;
	
	public CycleDetector() {
		this(new DefaultHashProvider());
	}
	
	public CycleDetector(IHashProvider hashProvider) {
		hashBuffer = new ArrayList<>();
		detectedCycles = new ArrayList<>();
		
		this.hashProvider = hashProvider;
		
		cycleDetectionListenerList = new ArrayList<>();
	}
	
	/**
	 * Add a result listener to the cycle detector
	 * 
	 * @param listener the listener to be added
	 * @return The added listener
	 */
	public CycleDetectedListener addListener(CycleDetectedListener listener) {
		cycleDetectionListenerList.add(listener);
		return listener;
	}
	
	/**
	 * Remove the given listener from the simulation engine and throw an exception if the
	 * listener can't be found
	 * 
	 * @param listener the listener to be removed
	 * @throws ListenerNotFoundException if the listener to be removed can't be found
	 */
	public void removeListener(CycleDetectedListener listener) throws ListenerNotFoundException {
		int idx = cycleDetectionListenerList.indexOf(listener);
		
		if(idx == -1) {
			throw new ListenerNotFoundException(listener);
		}
		
		cycleDetectionListenerList.remove(idx);
	}
	
	public void push(SimulationState newState) {
		HashValue newHashValue = hashProvider.hashCode(newState.getVoteResults());
		System.out.println(Arrays.toString(newHashValue.get()));
		System.out.println(newState.getVoteResults());
		int newCycleEnd = hashBuffer.size();
		
		for(int i = hashBuffer.size() - 1; i >= 0; i--) {
			if(hashBuffer.get(i).equals(newHashValue)) {
				CycleInfo cycle = new CycleInfo(i, newCycleEnd, newHashValue);
				detectedCycles.add(cycle);
				fireEventDetected(cycle);
				break;
			}
		}
		
		hashBuffer.add(newHashValue);
	}
	
	public List<CycleInfo> getDetectedCycles() {
		return Collections.unmodifiableList(detectedCycles);
	}
	
	private void fireEventDetected(CycleInfo info) {
		for(CycleDetectedListener l : cycleDetectionListenerList) {
			l.cycleDetected(info);
		}
	}
}
