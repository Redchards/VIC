package org.upmc.electisim;

import java.util.ArrayList;
import java.util.List;

// TODO : Maybe used the "stop" function to clear the buffer ?!?
public class SimulationEngine {

	private StateBuffer stateBuffer;
	private SimulationProfile simulationProfile;
	private int timestep = 36;
	private int committeeSize;
	private SimulationExecutionState executionState = SimulationExecutionState.STOPPED;
	
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
	
	public List<Candidate> step() {
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
		
		return simulationProfile.getVotingRule().getElectedCommittee(res, committeeSize);
	}
	
	// TODO : need to throw an exception when you can't rewind because we're stopped
	public List<Candidate> stepBack() throws InvalidStateSteppingException {
		stateBuffer.rewindStep();
		List<VoteResult> res = stateBuffer.getCurrent().getVoteResults();
		
		return simulationProfile.getVotingRule().getElectedCommittee(res, committeeSize);
	}
	
	public void pause() {
		executionState = SimulationExecutionState.PAUSED;
	}
	
	public void stop() {
		executionState = SimulationExecutionState.STOPPED;
	}
}
