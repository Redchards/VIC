package org.upmc.electisim.knowledge;

import java.util.List;

import org.upmc.electisim.IElectable;
import org.upmc.electisim.IVotingRule;
import org.upmc.electisim.SimulationState;
import org.upmc.electisim.StateBuffer;

/**
 * <p>A powerful knowledge dispenser aggregating every other dispenser in this package.</p>
 * <p>NOTE : if another knowledge dispenser is implemented, this class needs to be extended
 * to reflect the changes and gain the functionalities of the newly created dispenser, 
 * otherwise the new dispenser will not be usable in the future. For more information as to
 * why, please refer to the wiki.</p>
 */
public class OmniscientKnowledgeDispenser implements IStatefulKnowledgeDispenser, IRankingKnowledgeDispenser, IVotingRuleDispenser {

	/*
	 * (non-Javadoc)
	 * The underlying ranking knowledge dispenser
	 */
	private final RankingKnowledgeDispenser rankingDispenser;
	
	/*
	 * (non-Javadoc)
	 * The underlying stateful knowledge dispenser
	 */
	private final StatefulKnowledgeDispenser statefulDispenser;
	
	/*
	 * (non-Javadoc)
	 * The underlying voting rule knowledge dispenser
	 */
	private final VotingRuleDispenser ruleDispenser;
	
	/**
	 * Build an omniscient knowledge dispenser from a state buffer and a voting rule
	 * 
	 * @param stateBuffer the state buffer of the simulation
	 * @param rule the voting rule used by the simulation
	 */
	public OmniscientKnowledgeDispenser(StateBuffer stateBuffer, IVotingRule rule) {
		SimulationState state = stateBuffer.getCurrent();
		
		this.rankingDispenser = new RankingKnowledgeDispenser(state != null ? state.getElectionResult() : null);
		this.statefulDispenser = new StatefulKnowledgeDispenser(stateBuffer);
		this.ruleDispenser = new VotingRuleDispenser(rule);
	}
	
	/*
	 * (non-Javadoc)
	 * Get the ranking of all the candidates from the last state
	 * 
	 * @see org.upmc.electisim.knowledge.IRankingKnowledgeDispenser#getLastCandidateRanking()
	 */
	@Override
	public List<IElectable> getLastCandidateRanking() {
		return this.rankingDispenser.getLastCandidateRanking();
	}

	/*
	 * (non-Javadoc)
	 * Get the ranking of the elected committee from the last state
	 * 
	 * @see org.upmc.electisim.knowledge.IRankingKnowledgeDispenser#getLastCommitteeRanking()
	 */
	@Override
	public List<IElectable> getLastCommitteeRanking() {
		return this.rankingDispenser.getLastCommitteeRanking();
	}

	/*
	 * (non-Javadoc)
	 * Get the last simulation state
	 * 
	 * @see org.upmc.electisim.knowledge.IStatefulKnowledgeDispenser#getLastSimulationState()
	 */
	@Override
	public SimulationState getLastSimulationState() {
		return this.statefulDispenser.getLastSimulationState();
	}

	/*
	 * (non-Javadoc)
	 * Get the simulation state at the specified index
	 * 
	 * @see org.upmc.electisim.knowledge.IStatefulKnowledgeDispenser#getSimulationState(int)
	 */
	@Override
	public SimulationState getSimulationState(int idx) {
		return this.statefulDispenser.getSimulationState(idx);
	}

	/*
	 * (non-Javadoc)
	 * Get the voting rule
	 * 
	 * @see org.upmc.electisim.knowledge.IVotingRuleDispenser#getVotingRule()
	 */
	@Override
	public IVotingRule getVotingRule() {
		return this.ruleDispenser.getVotingRule();
	}

	/*
	 * (non-Javadoc)
	 * Checks wether the current iteration is the first or not
	 * 
	 * @see org.upmc.electisim.knowledge.IStatefulKnowledgeDispenser#firstIteration()
	 */
	@Override
	public boolean firstIteration() {
		return this.statefulDispenser.firstIteration();
	}

}
