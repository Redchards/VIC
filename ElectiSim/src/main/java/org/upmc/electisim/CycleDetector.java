package org.upmc.electisim;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EventListener;
import java.util.List;

public class CycleDetector {
	private List<HashValue> hashBuffer;
	private List<CycleInfo> detectedCycles;
	
	private IHashProvider hashProvider;
	
	public interface CycleDetectedListener extends EventListener {
		public void cycleDetected(CycleInfo info);
	}
	
	private List<CycleDetectedListener> cycleDetectionListenerList;
	
	public CycleDetector() {
		this(new CRC32HashProvider());
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
		int newCycleEnd = hashBuffer.size();
		
		for(int i = hashBuffer.size() - 1; i >= 0; i--) {
			if(hashBuffer.get(i).equals(newHashValue)) {
				CycleInfo cycle = new CycleInfo(i, newCycleEnd);
				detectedCycles.add(cycle);
				fireEventDetected(cycle);
				break;
			}
		}
		
		hashBuffer.add(newHashValue);
	}
	
	public boolean cycleAlreadyDetected(CycleInfo info) {
		boolean res = false;
		System.out.println("Cycle similarity detection");
		for(CycleInfo cycle : detectedCycles) {
			if(cycle.getCycleLength() != info.getCycleLength() || cycle.equals(info)) {
				continue;
			}
			boolean sameCycle = true;
			System.out.println("Info : " + info.toString());
			System.out.println(cycle);
			for(int i = 0; i < cycle.getCycleLength(); i++) {
				if (!hashBuffer.get(cycle.getStart() + i).equals(hashBuffer.get(info.getStart() + i))) {
					sameCycle = false;
					break;
				}
			}
			
			res = res || sameCycle;
		}
		
		return res;
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
