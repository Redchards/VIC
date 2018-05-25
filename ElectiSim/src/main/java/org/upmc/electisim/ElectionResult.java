package org.upmc.electisim;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.upmc.electisim.utils.MapUtils;

/**
 * A class representing the result of an election, containing the score map and the elected
 * committee. Such objects are in general produced by the voting rules, implementing the interface
 * {@link org.upmc.electisim.IVotingRule}.
 */
public class ElectionResult {
	
	/*
	 * (non-Javadoc)
	 * The map of scores of the current election process
	 */
	private final Map<IElectable, Integer> scoreMap;
	
	/*
	 * (non-Javadoc)
	 * The elected committee during the current election process
	 */
	private final List<IElectable> electedCommittee;
	
	/**
	 * Build an election result from a map of scores and an elected committee
	 * 
	 * @param scoreMap the map of scores
	 * @param electedCommittee the elected committee
	 */
	public ElectionResult(Map<IElectable, Integer> scoreMap, List<IElectable> electedCommittee) {
		this.scoreMap = scoreMap;
		this.electedCommittee = new ArrayList<>(electedCommittee);
	}
	
	/**
	 * Retrieve the score of a candidate
	 * 
	 * @param candidate the candidate of which we want the score
	 * @return The score of the candidate
	 */
	public int getCandidateScore(IElectable candidate) {
		return scoreMap.get(candidate);
	}
	
	/**
	 * @return An iterator on the candidates of this election
	 */
	public Iterator<IElectable> candidateIterator() {
		return scoreMap.keySet().iterator();
	}
	
	/**
	 * Generate ascending candidate ranking
	 * 
	 * @return An ascending candidate ranking
	 */
	public List<IElectable> generateAscendingCandidateRanking() {
		List<Map.Entry<IElectable, Integer>> sortedSet = MapUtils.sortByValue(scoreMap);
		List<IElectable> res = new ArrayList<>();
		
		for(Map.Entry<IElectable, Integer> entry : sortedSet) {
			res.add(entry.getKey());
		}
		
		return res;
	}
	
	/**
	 * Generate descending candidate ranking
	 * 
	 * @return An descending candidate ranking
	 */
	public List<IElectable> generateDescendingCandidateRanking() {
		List<IElectable> res = this.generateAscendingCandidateRanking();
		Collections.reverse(res);
		
		return res;
	}
	
	/**
	 * @return The elected committee of this election
	 */
	public List<IElectable> getElectedCommittee() {
		return Collections.unmodifiableList(electedCommittee);
	}
	
	/**
	 * @param k the number of best elements we want
	 * @return the k bests candidate in this vote
	 */
	public List<IElectable> getKBests(int k) {
		return this.generateDescendingCandidateRanking().subList(0, k);
	}
	
	/**
	 * @return A list of the effective electable entities for which the agents voted
	 */
	public List<IElectable> getElectableList() {
		List<IElectable> res = new ArrayList<>();
		res.addAll(this.scoreMap.keySet());
		return res;
	}
	
	//TODO : not working with Committee
	// TODO : do we really need it ?
	@Override
	public boolean equals(Object obj) {
		ElectionResult other = (ElectionResult) obj;
//		System.out.println(this.scoreMap.toString()+"\n compared to \n"+other.scoreMap.toString());
//		System.out.println(this.scoreMap.equals(other.scoreMap)+" elected : "+this.electedCommittee.equals(other.electedCommittee));
		return this.scoreMap.equals(other.scoreMap) && this.electedCommittee.equals(other.electedCommittee);
	}
}
