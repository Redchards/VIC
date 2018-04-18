package org.upmc.electisim;

import java.util.ArrayList;
import java.util.List;

public class OmniscientBestResponseStrategy extends ABestReponseAgentStrategy {

	public OmniscientBestResponseStrategy(StateBuffer buffer, IVotingRule rule) {
		super(buffer, rule);
	}

	@Override
	public VoteResult executeVote(Agent agent, List<Candidate> candidateList, int committeeSize) {
		List<List<Candidate>> possibleCommittees = generateCombinations(candidateList, committeeSize);		
	}
	
	private List<List<Candidate>> generateCombinations(List<Candidate> candidateList, int committeeSize) {
		return generateCombinationsAux(candidateList, 0, (candidateList.size() - committeeSize), committeeSize, new ArrayList<List<Candidate>>(), new ArrayList<Candidate>());
	}
	
	private List<List<Candidate>> generateCombinationsAux(List<Candidate> candidateList, int begin, int end, int level, List<List<Candidate>> l, List<Candidate> tmp) {
		if(level == 0) {
			l.add(tmp);
		}
		
		for(int i = begin; i < end; i++) {
			tmp.set(level - 1, candidateList.get(i));
			generateCombinationsAux(candidateList, begin + 1, end + 1, level - 1, l, new ArrayList<Candidate>(tmp));
		}
		
		return l;
	}

}
