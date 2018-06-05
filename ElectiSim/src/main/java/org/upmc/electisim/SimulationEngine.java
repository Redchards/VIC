package org.upmc.electisim;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.upmc.electisim.knowledge.OmniscientKnowledgeDispenser;
import org.upmc.electisim.output.InvalidExtensionException;
import org.upmc.electisim.output.StateBufferFileWriter;
import org.upmc.electisim.output.StateFileWriter;
import org.upmc.electisim.utils.EmptyBufferException;
import org.upmc.electisim.utils.SimulationEngineConfigDefaults;

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
	
	/*
	 * (non-Javadoc)
	 * The cycle detector for the simulation
	 */
	private CycleDetector cycleDetector;
	
	/**
	 * Build a simulation engine from a simulation profile, defaulting the buffer size, the
	 * timestep and the iteration count
	 * 
	 * @param profile the simulation profile to use for this simulation
	 */
	public SimulationEngine(SimulationProfile profile) {
		this(profile, SimulationEngineConfigDefaults.getDefaultBufferSize());
	}
	
	/**
	 * Build a simulation engine from a simulation profile and a buffer size, defaulting the
	 * timestep and the iteration count
	 * 
	 * @param profile the simulation profile to use for this simulation
	 * @param bufferSize the size of the state buffer
	 */
	public SimulationEngine(SimulationProfile profile, int bufferSize) {
		this(profile, bufferSize, SimulationEngineConfigDefaults.getDefaultTimestep());
	}
	
	/**
	 * Build a simulation engine from a simulation profile, a buffer size and a timestep, defaulting
	 * the iteration count
	 * 
	 * @param profile the simulation profile to use for this simulation
	 * @param bufferSize the size of the state buffer
	 * @param timestep the timestep of the simulation
	 */
	public SimulationEngine(SimulationProfile profile, int bufferSize, int timestep) {
		this(profile, bufferSize, timestep, SimulationEngineConfigDefaults.getDefaultStepCount());
	}
	
	/**
	 * Build a simulation engine from a simulation profile, a buffer size, a timestep and
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
		this.cycleDetector = new CycleDetector();

		this.currentIteration = 0;
	}
	
	/**
	 * Add a result listener to the simulation engine
	 * 
	 * @param listener the listener to be added
	 * @return The added listener
	 */
	public ResultListener addListener(ResultListener listener) {
		listenerList.add(listener);
		return listener;
	}
	
	/**
	 * Remove the given listener from the simulation engine and throw an exception if the
	 * listener can't be found
	 * 
	 * @param listener the listener to be removed
	 * @throws ListenerNotFoundException if the listener to be removed can't be found
	 */
	public void removeListener(ResultListener listener) throws ListenerNotFoundException {
		int idx = listenerList.indexOf(listener);
		
		if(idx == -1) {
			throw new ListenerNotFoundException(listener);
		}
		
		listenerList.remove(idx);
	}
	
	/**
	 * @return The timestep of the simulation
	 */
	public int getTimestep() {
		return timestep;
	}
	
	/**
	 * Set the timestep of the simulation
	 * 
	 * @param timestep the timestep to use
	 */
	public void setTimestep(int timestep) {
		this.timestep = timestep;
	}
	
	/**
	 * @return The maximum iteration count
	 */
	public int getIterationCount() {
		return iterationCount;
	}
	
	/**
	 * Set the maximum iteration count
	 * 
	 * @param count the maximum iteration count
	 */
	public void setIterationCount(int count) {
		this.iterationCount = count;
	}
	
	/**
	 * @return The profile of the simulation
	 */
	public SimulationProfile getSimulationProfile() {
		return simulationProfile;
	}
	
	/**
	 * TODO : Isn't it a little bit dangerous and useless to expose the state buffer ?
	 * 
	 * @return The state buffer
	 */
	public StateBuffer getStateBuffer() {
		return stateBuffer;
	}
	
	/**
	 * @return The execution state
	 */
	public SimulationExecutionState getExecutionState() {
		return executionState;
	}
	
	/**
	 * @return The cycle detector
	 */
	public CycleDetector getCycleDetector() {
		return cycleDetector;
	}
	
	public int getCurrentIteration() {
		return currentIteration;
	}
	
	public CycleDetector.CycleDetectedListener addCycleDetectionListener(CycleDetector.CycleDetectedListener listener) {
		cycleDetector.addListener(listener);
		return listener;
	}
	
	public void removeCycleDetectionListener(CycleDetector.CycleDetectedListener listener) throws ListenerNotFoundException {
		cycleDetector.removeListener(listener);
	}
	
	/**
	 * Execute one step of the simulation, or advance one step if one is already buffered
	 */
	public void step() {		
		System.out.println("**************** SimulationEngine : step == "+currentIteration);
		
		if(executionState == SimulationExecutionState.STOPPED) {
			pause();
			stateBuffer.clearBuffer();
			currentIteration = 0;
			this.cycleDetector = new CycleDetector();

		}

		
		ElectionResult electionResult;
		OmniscientKnowledgeDispenser dispenser = new OmniscientKnowledgeDispenser(stateBuffer, simulationProfile.getVotingRule());

		SimulationState lastSimulationState = null;
		SimulationState currentState = null;
		
		try {
			lastSimulationState = stateBuffer.getLast();
			currentState = stateBuffer.getCurrent();
		} catch (EmptyBufferException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		if(lastSimulationState == currentState) {
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

			SimulationState newState = new SimulationState(simulationProfile, res, electionResult);
			stateBuffer.push(newState);
			cycleDetector.push(newState);
			
			
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
		
		currentIteration++;
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
		
		System.out.println(lastSimulationState);
		System.out.println(currentState);
		
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
	}
	
	/**
	 * Step back in the simulation
	 * 
	 * @throws InvalidStateSteppingException if the stepping is invalide
	 */
	public void stepBack() throws InvalidStateSteppingException {
		if(executionState == SimulationExecutionState.STOPPED) {
			return;
		}
				
		stateBuffer.rewindStep();
		List<AgentVote> res;
		res = stateBuffer.getCurrent().getVoteResults();
		
		ElectionResult electionResult = simulationProfile.getVotingRule().getElectionResult(res, simulationProfile.getCommitteeSize());
		currentIteration--;

		this.fireResultProducedEvent(electionResult);		
	}
	
	/**
	 * Launch the simulation
	 * 
	 * @throws InterruptedException if the thread is interrupted before the execution is finished
	 */
	public void run() throws InterruptedException {
		executionState = SimulationExecutionState.RUNNING;
		
		if(executionState == SimulationExecutionState.STOPPED) {
			stateBuffer.clearBuffer();
			currentIteration = 0;
			this.cycleDetector = new CycleDetector();

		}
		
		long startTime = System.currentTimeMillis();
		long endTime = 0;
		
		for(int i = currentIteration; (i < iterationCount || iterationCount == 0) && isRunning(); i++) {
			/*for(Agent a : simulationProfile.getAgentList()) {
				System.out.println(a.getPreferences().getPreferenceList().toString());
			}
			System.out.println(simulationProfile.getPreferenceType());*/
			step();
			System.out.println("It : " + i);
			
			SimulationState currentState = stateBuffer.getCurrent();

			if(currentState != null && stateBuffer.getPrevious() != null
			   && currentState.getElectionResult().equals(stateBuffer.getPrevious().getElectionResult())) {
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
	
	public void runBack() throws InterruptedException {
		executionState = SimulationExecutionState.RUNNING;
		
		long startTime = System.currentTimeMillis();
		long endTime = 0;
		
		for(int i = currentIteration; (iterationCount >= 0) && isRunning(); i--) {
			/*for(Agent a : simulationProfile.getAgentList()) {
				System.out.println(a.getPreferences().getPreferenceList().toString());
			}
			System.out.println(simulationProfile.getPreferenceType());*/
			try {
				stepBack();
			} catch (InvalidStateSteppingException e) {
				pause();
				return;
			}
			System.out.println("It : " + i);
						
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
	
	/**
	 * Pause the simulation.
	 * Do note that the simulation will not stop until the current step is complete
	 */
	public void pause() {
		executionState = SimulationExecutionState.PAUSED;
	}
	
	/**
	 * Stop the simulation.
	 * Do note that the simulation will not stop until the current step is complete
	 * TODO : maybe change that ? It doesn't make much sense
	 */
	public void stop() {
		executionState = SimulationExecutionState.STOPPED;
	}
	
	/**
	 * @return The value true if the simulation is running, false otherwise
	 */
	public boolean isRunning() {
		return executionState.equals(SimulationExecutionState.RUNNING);
	}
	
	/**
	 * @return The value true if the simulation is paused, false otherwise
	 */
	public boolean isPaused() {
		return executionState.equals(SimulationExecutionState.PAUSED);
	}
	
	/**
	 * @return The value true if the simulation is stopped, false otherwise
	 */
	public boolean isStopped() {
		return executionState.equals(SimulationExecutionState.STOPPED);
	}
	
	public void saveCurrentState(File file) throws IOException, InvalidExtensionException, EmptyBufferException {
		try(StateFileWriter writer = new StateFileWriter(file)){
			writer.writeState(stateBuffer.getCurrent());
			writer.flush();
		}
	}
	
	/**
	 * Save the current state of the simulation
	 * 
	 * @param filename the file name to use
	 * @throws IOException if the file is not found or can't be read
	 * @throws InvalidExtensionException if the file extension is invalid
	 * @throws EmptyBufferException 
	 */
	public void saveCurrentState(String filename) throws IOException, InvalidExtensionException, EmptyBufferException {
		saveCurrentState(new File(filename));
	}
	
	public void saveCurrentStateBuffer(File dir) throws Exception {
		try(StateBufferFileWriter writer = new StateBufferFileWriter(dir.getAbsolutePath())){
			writer.writeBuffer(stateBuffer);
		}
	}
	
	public void saveCurrentStateBuffer(String directoryName) throws Exception {
		saveCurrentStateBuffer(new File(directoryName));
	}
	
	/**
	 * Fire an even when a result is produced
	 * 
	 * @param electionResult the election result produced
	 */
	protected void fireResultProducedEvent(ElectionResult electionResult) {
		for(ResultListener l : listenerList) {
			l.resultProduced(electionResult);
		}
	}
}
