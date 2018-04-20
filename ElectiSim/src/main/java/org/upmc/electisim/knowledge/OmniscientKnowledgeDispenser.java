package org.upmc.electisim.knowledge;

import java.util.List;

import org.upmc.electisim.Candidate;
import org.upmc.electisim.IVotingRule;
import org.upmc.electisim.SimulationState;
import org.upmc.electisim.StateBuffer;

public class OmniscientKnowledgeDispenser implements IStatefulKnowledgeDispenser, IRankingKnowledgeDispenser, IVotingRuleDispenser {

	private final RankingKnowledgeDispenser rankingDispenser;
	private final StatefulKnowledgeDispenser statefulDispenser;
	private final VotingRuleDispenser ruleDispenser;
	
	public OmniscientKnowledgeDispenser(StateBuffer stateBuffer, IVotingRule rule) {
		this.rankingDispenser = new RankingKnowledgeDispenser(stateBuffer.getCurrent().getElectionResult());
		this.statefulDispenser = new StatefulKnowledgeDispenser(stateBuffer);
		this.ruleDispenser = new VotingRuleDispenser(rule);
	}
	
	@Override
	public List<Candidate> getLastCandidateRanking() {
		return this.rankingDispenser.getLastCandidateRanking();
	}

	@Override
	public List<Candidate> getLastCommitteeRanking() {
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

}
