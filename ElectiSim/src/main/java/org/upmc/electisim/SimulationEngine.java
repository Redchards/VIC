package org.upmc.electisim;

import java.io.IOException;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.upmc.electisim.knowledge.OmniscientKnowledgeDispenser;
import org.upmc.electisim.output.InvalidExtensionException;
import org.upmc.electisim.output.StateFileWriter;
import org.upmc.electisim.utils.EmptyBufferException;
import org.upmc.electisim.utils.SimulationEngineConfigHelper;

/**
 * The simulation engine, used to manage the simulation execution.
 */
public class SimulationEngine {
	
	/**
	 * The type used for the even listener called when a result is produced (after each step of the simulation)
	 */
	public interface ResultListener extends EventListener {
		/**
		 * The method called when a result is produced
		 * 
		 * @param electionResult the election result produced by the execution
		 */
		public void resultProduced(ElectionResult electionResult);
	}

	/*
	 * (non-Javadoc)
	 * The state buffer
	 */
	private StateBuffer stateBuffer;
	
	/*
	 * (non-Javadoc)
	 * The simulation profile
	 */
	private SimulationProfile simulationProfile;
	
	/*
	 * (non-Javadoc)
	 * The timestep for the simulation expressed in ms
	 */
	private int timestep = 36;
	
	/*
	 * (non-Javadoc)
	 * The number of iterations to execute before stopping.
	 * An interation count of 0 means that we do not limit the number of iterations
	 */
	private int iterationCount;
	
	/*
	 * (non-Javadoc)
	 * The current execution state
	 * @see org.upmc.electisim.SimulationExecutionState
	 */
	private SimulationExecutionState executionState = SimulationExecutionState.STOPPED;
	
	/*
	 * (non-Javadoc)
	 * The list of listeners called at every result produced
	 */
	private List<ResultListener> listenerList;
	
	/*
	 * (non-Javadoc)
	 * The current iteration count
	 */
	private int currentIteration = 0;
	
	/**
	 * Builds a simulation engine from a simulation profile, defaulting the buffer size, the
	 * timestep and the iteration count
	 * 
	 * @param profile the simulation profile to use for this simulation
	 */
	public SimulationEngine(SimulationProfile profile) {
		this(profile, SimulationEngineConfigHelper.getDefaultBufferSize());
	}
	
	/**
	 * Builds a simulation engine from a simulation profile and a buffer size, defaulting the
	 * timestep and the iteration count
	 * 
	 * @param profile the simulation profile to use for this simulation
	 * @param bufferSize the size of the state buffer
	 */
	public SimulationEngine(SimulationProfile profile, int bufferSize) {
		this(profile, bufferSize, SimulationEngineConfigHelper.getDefaultTimestep());
	}
	
	/**
	 * Builds a simulation engine from a simulation profile, a buffer size and a timestep, defaulting
	 * the iteration count
	 * 
	 * @param profile the simulation profile to use for this simulation
	 * @param bufferSize the size of the state buffer
	 * @param timestep the timestep of the simulation
	 */
	public SimulationEngine(SimulationProfile profile, int bufferSize, int timestep) {
		this(profile, bufferSize, timestep, SimulationEngineConfigHelper.getDefaultStepCount());
	}
	
	/**
	 * Builds a simulation engine from a simulation profile, a buffer size, a timestep and
	 * an iteration count
	 * 
	 * @param profile the simulation profile to use for this simulation
	 * @param bufferSize the size of the state buffer
	 * @param timestep the timestep of the simulation
	 * @param stepCount the iteration count
	 */
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

		SimulationState lastSimulationState = null;
		
		try {
			lastSimulationState = stateBuffer.getLast();
		} catch (EmptyBufferException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		if(lastSimulationState == stateBuffer.getCurrent()) {
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
