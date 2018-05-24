package org.upmc.electisim;

import java.io.IOException;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.upmc.electisim.knowledge.OmniscientKnowledgeDispenser;
import org.upmc.electisim.output.InvalidExtensionException;
import org.upmc.electisim.output.StateFileWriter;
import org.upmc.electisim.utils.SimulationEngineConfigHelper;

public class SimulationEngine {
	
	public interface ResultListener extends EventListener {
		public void resultProduced(ElectionResult electionResult);
	}

	private StateBuffer stateBuffer;
	private SimulationProfile simulationProfile;
	
	// The timestep is in ms.
	private int timestep = 36;
	private int iterationCount;
	private SimulationExecutionState executionState = SimulationExecutionState.STOPPED;
	private List<ResultListener> listenerList;
	
	private int currentIteration = 0;
	
	public SimulationEngine(SimulationProfile profile) {
		this(profile, SimulationEngineConfigHelper.getDefaultBufferSize());
	}
	
	public SimulationEngine(SimulationProfile profile, int bufferSize) {
		this(profile, bufferSize, SimulationEngineConfigHelper.getDefaultTimestep());
	}
	
	public SimulationEngine(SimulationProfile profile, int bufferSize, int timestep) {
		this(profile, bufferSize, timestep, SimulationEngineConfigHelper.getDefaultStepCount());
	}
	
	public SimulationEngine(SimulationProfile profile, int bufferSize, int timestep, int stepCount) {
		this.stateBuffer = new StateBuffer(bufferSize);
		this.simulationProfile = profile;
		this.timestep = timestep;
		this.listenerList = new ArrayList<>();
		this.iterationCount = stepCount;

		this.currentIteration = 0;
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
	
	public int getIterationCount() {
		return iterationCount;
	}
	
	public void setIterationCount(int count) {
		this.iterationCount = count;
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
		
		System.out.println("**************** SimulationEngine : step == "+currentIteration);
		
		if(executionState == SimulationExecutionState.STOPPED) {
			pause();
			stateBuffer.clearBuffer();
		}
		
		ElectionResult electionResult;
		OmniscientKnowledgeDispenser dispenser = new OmniscientKnowledgeDispenser(stateBuffer, simulationProfile.getVotingRule());


		if(stateBuffer.getLast() == stateBuffer.getCurrent()) {
			List<IElectable> candidateList = simulationProfile.getCandidateList();
			List<AgentVote> res = new ArrayList<>();
			
			System.out.println("****Votes  : ");
			for(Agent agent : simulationProfile.getAgentList()) {
				System.out.println("Agent : "+agent.getName());
				AgentVote vote = simulationProfile.getVotingStrategy().executeVote(agent, dispenser, candidateList, simulationProfile.getCommitteeSize());
				res.add(vote);

//				res.add(simulationProfile.getVotingStrategy().executeVote(agent, dispenser, candidateList, committeeSize));
				for(IElectable candidate : vote.getScoreMap().keySet()){
					System.out.println("Candidate : "+candidate.getName()+" score : "+vote.getScoreMap().get(candidate));
				}
			}

			electionResult = simulationProfile.getVotingRule().getElectionResult(res, simulationProfile.getCommitteeSize());

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
		
		System.out.println("****Results : ");
		//for(IElectable candidate : simulationProfile.getCandidateList()) {
		for(IElectable candidate : electionResult.generateDescendingCandidateRanking()) {
			System.out.println("Candidate : "+candidate.getName()+" score : "+electionResult.getCandidateScore(candidate));
		}
		System.out.println("hello");
		
		System.out.println("***** Elected Committee : ");
		for(IElectable candidate : electionResult.getElectedCommittee()){
			System.out.println(candidate.getName());
		}
		
		//results saving
//		try {
//			saveCurrentState("tests\\3 divers - comité de 2\\Borda\\"+simulationProfile.getVotingRule().getClass().getSimpleName()+"_2 against 1_iteration"+currentIteration+".csv");
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (InvalidExtensionException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		currentIteration++;
	}
	
	public void stepBack() throws InvalidStateSteppingException {
		if(executionState == SimulationExecutionState.STOPPED) {
			return;
		}
		
		stateBuffer.rewindStep();
		List<AgentVote> res = stateBuffer.getCurrent().getVoteResults();
		
		ElectionResult electionResult = simulationProfile.getVotingRule().getElectionResult(res, simulationProfile.getCommitteeSize());
		this.fireResultProducedEvent(electionResult);
		
		currentIteration--;
	}
	
	public void run() throws InterruptedException {
		executionState = SimulationExecutionState.RUNNING;
		
		long startTime = System.currentTimeMillis();
		long endTime = 0;
		
		for(int i = currentIteration; (i < iterationCount || iterationCount == 0) && isRunning(); i++) {
			/*for(Agent a : simulationProfile.getAgentList()) {
				System.out.println(a.getPreferences().getPreferenceList().toString());
			}
			System.out.println(simulationProfile.getPreferenceType());*/
			step();
			System.out.println("It : " + i);
			
			
			if(stateBuffer.getCurrent() != null && stateBuffer.getPrevious() != null
			   && stateBuffer.getCurrent().getElectionResult().equals(stateBuffer.getPrevious().getElectionResult())) {
				pause();
				System.out.println("out");
				return;
			}
			
			endTime = System.currentTimeMillis();
			
			long delta = (endTime - startTime);
			System.out.println(delta);
			if(delta < timestep && timestep != 0) {
				TimeUnit.MILLISECONDS.sleep(timestep - delta);
			}
			System.out.println("hey, wakeup");
			
			endTime = System.currentTimeMillis();
			startTime = endTime;
		}
	}
		
	public void pause() {
		executionState = SimulationExecutionState.PAUSED;
	}
	
	public void stop() {
		executionState = SimulationExecutionState.STOPPED;
	}
	
	public boolean isRunning() {
		return executionState.equals(SimulationExecutionState.RUNNING);
	}
	
	public boolean isPaused() {
		return executionState.equals(SimulationExecutionState.PAUSED);
	}
	
	public boolean isStopped() {
		return executionState.equals(SimulationExecutionState.STOPPED);
	}
	
	public void saveCurrentState(String filename) throws IOException, InvalidExtensionException {
		try(StateFileWriter writer = new StateFileWriter(filename)){
			System.out.println(stateBuffer.getCurrent().getElectionResult().getElectedCommittee().toString());
			writer.writeState(stateBuffer.getCurrent());
			writer.flush();
		}
	}
	
	protected void fireResultProducedEvent(ElectionResult electionResult) {
		for(ResultListener l : listenerList) {
			l.resultProduced(electionResult);
		}
	}
}
