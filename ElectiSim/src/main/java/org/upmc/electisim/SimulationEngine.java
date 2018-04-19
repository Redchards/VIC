package org.upmc.electisim;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;

public class SimulationEngine {
	
	public interface ResultListener extends EventListener {
		public void resultProduced(List<Candidate> committee);
	}

	private StateBuffer stateBuffer;
	private SimulationProfile simulationProfile;
	
	// The timestep is in ms.
	private int timestep = 36;
	private int committeeSize;
	private SimulationExecutionState executionState = SimulationExecutionState.STOPPED;
	private List<ResultListener> listenerList;
	
	public SimulationEngine(SimulationProfile profile, int committeeSize) {
		this.stateBuffer = new StateBuffer();
		this.simulationProfile = profile;
		this.committeeSize = committeeSize;
	}
	
	public SimulationEngine(SimulationProfile profile, int committeeSize, int bufferSize) {
		this.stateBuffer = new StateBuffer(bufferSize);
		this.simulationProfile = profile;
		this.committeeSize = committeeSize;
	}
	
	public SimulationEngine(SimulationProfile profile, int committeeSize, int bufferSize, int timestep) {
		this.stateBuffer = new StateBuffer(bufferSize);
		this.simulationProfile = profile;
		this.timestep = timestep;
		this.committeeSize = committeeSize;
	}
	
	public void addListener(ResultListener listener) {
		listenerList.add(listener);
	}
	
	public void removeListener(ResultListener listener) throws ListenerNotFoundException {
		int idx = listenerList.indexOf(listener);
		
		if(idx == -1) {
			throw new ListenerNotFoundException(listener);
		}
		
		listenerList.remove(idx);
	}
	
	public int getTimestep() {
		return timestep;
	}
	
	public void setTimestep(int timestep) {
		this.timestep = timestep;
	}
	
	public SimulationProfile getSimulationProfile() {
		return simulationProfile;
	}
	
	// TODO : FIX, we can't just have such a simple functions, it entails tons of other things !
	public void setSimulationProfile(SimulationProfile profile) {
		this.simulationProfile = profile;
	}
	
	public StateBuffer getStateBuffer() {
		return stateBuffer;
	}
	
	public SimulationExecutionState getExecutionState() {
		return executionState;
	}
	
	public void step() {
		if(executionState == SimulationExecutionState.STOPPED) {
			pause();
			stateBuffer.clearBuffer();
		}
		List<VoteResult> res;

		if(stateBuffer.getLast() == stateBuffer.getCurrent()) {
			List<Candidate> candidateList = simulationProfile.getCandidateList();
			res = new ArrayList<>();
			
			for(Agent agent : simulationProfile.getAgentList()) {
				res.add(simulationProfile.getVotingStrategy().executeVote(agent, candidateList, committeeSize));
			}
			
			stateBuffer.push(new SimulationState(simulationProfile, res));
		}
		else {
			try {
				stateBuffer.advanceStep();
			} catch (InvalidStateSteppingException e) {
				// TODO Should never be here, something very wrong happened otherwise (programming error)
				// Put a better exception !!!
				e.printStackTrace();
			}
			res = stateBuffer.getCurrent().getVoteResults();
		}
		
		List<Candidate> electedCommittee = simulationProfile.getVotingRule().getElectedCommittee(res, committeeSize);
		this.fireResultProducedEvent(electedCommittee);
	}
	
	public void stepBack() throws InvalidStateSteppingException {
		if(executionState == SimulationExecutionState.STOPPED) {
			return;
		}
		
		stateBuffer.rewindStep();
		List<VoteResult> res = stateBuffer.getCurrent().getVoteResults();
		
		List<Candidate> electeCommittee = simulationProfile.getVotingRule().getElectedCommittee(res, committeeSize);
		this.fireResultProducedEvent(electeCommittee);
	}
	
	public void run() {
		for(int i = 0; i < 1000; i++) {
			step();
		}
	}
		
	public void pause() {
		executionState = SimulationExecutionState.PAUSED;
	}
	
	public void stop() {
		executionState = SimulationExecutionState.STOPPED;
	}
	
	protected void fireResultProducedEvent(List<Candidate> committee) {
		for(ResultListener l : listenerList) {
			l.resultProduced(committee);
		}
	}
}
