package org.upmc.electisim;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;

public class SimulationEngine {
	
	public interface ResultListener extends EventListener {
		public void resultProduced(ElectionResult electionResult);
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
		
		ElectionResult electionResult;


		if(stateBuffer.getLast() == stateBuffer.getCurrent()) {
			List<Candidate> candidateList = simulationProfile.getCandidateList();
			List<VoteResult> res = new ArrayList<>();
			
			for(Agent agent : simulationProfile.getAgentList()) {
				res.add(simulationProfile.getVotingStrategy().executeVote(agent, candidateList, committeeSize));
			}

			electionResult = simulationProfile.getVotingRule().getElectionResult(res, committeeSize);

			stateBuffer.push(new SimulationState(simulationProfile, res, electionResult));
		}
		else {
			try {
				stateBuffer.advanceStep();
			} catch (InvalidStateSteppingException e) {
				// TODO Should never be here, something very wrong happened otherwise (programming error)
				// Put a better exception !!!
				e.printStackTrace();
			}
			electionResult = stateBuffer.getCurrent().getElectionResult();
		}
		
		this.fireResultProducedEvent(electionResult);
	}
	
	public void stepBack() throws InvalidStateSteppingException {
		if(executionState == SimulationExecutionState.STOPPED) {
			return;
		}
		
		stateBuffer.rewindStep();
		List<VoteResult> res = stateBuffer.getCurrent().getVoteResults();
		
		ElectionResult electionResult = simulationProfile.getVotingRule().getElectionResult(res, committeeSize);
		this.fireResultProducedEvent(electionResult);
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
	
	protected void fireResultProducedEvent(ElectionResult electionResult) {
		for(ResultListener l : listenerList) {
			l.resultProduced(electionResult);
		}
	}
}
