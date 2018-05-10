package org.upmc.electisim.knowledge;

import java.util.List;

import org.upmc.electisim.IElectable;
import org.upmc.electisim.IVotingRule;
import org.upmc.electisim.SimulationState;
import org.upmc.electisim.StateBuffer;

public class OmniscientKnowledgeDispenser implements IStatefulKnowledgeDispenser, IRankingKnowledgeDispenser, IVotingRuleDispenser {

	private final RankingKnowledgeDispenser rankingDispenser;
	private final StatefulKnowledgeDispenser statefulDispenser;
	private final VotingRuleDispenser ruleDispenser;
	
	public OmniscientKnowledgeDispenser(StateBuffer stateBuffer, IVotingRule rule) {
		SimulationState state = stateBuffer.getCurrent();
		
		this.rankingDispenser = new RankingKnowledgeDispenser(state != null ? state.getElectionResult() : null);
		this.statefulDispenser = new StatefulKnowledgeDispenser(stateBuffer);
		this.ruleDispenser = new VotingRuleDispenser(rule);
	}
	
	@Override
	public List<IElectable> getLastCandidateRanking() {
		return this.rankingDispenser.getLastCandidateRanking();
	}

	@Override
	public List<IElectable> getLastCommitteeRanking() {
		return this.rankingDispenser.getLastCommitteeRanking();
	}

	@Override
	public SimulationState getLastSimulationState() {
		return this.statefulDispenser.getLastSimulationState();
	}

	@Override
	public SimulationState getSimulationState(int idx) {
		return this.statefulDispenser.getSimulationState(idx);
	}

	@Override
	public IVotingRule getVotingRule() {
		return this.ruleDispenser.getVotingRule();
	}

	@Override
	public boolean firstIteration() {
		return this.statefulDispenser.firstIteration();
	}

}
