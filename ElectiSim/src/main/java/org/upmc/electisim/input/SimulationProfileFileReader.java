package org.upmc.electisim.input;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.upmc.electisim.Agent;
import org.upmc.electisim.BlocVotingRule;
import org.upmc.electisim.Candidate;
import org.upmc.electisim.IAgentStrategy;
import org.upmc.electisim.IVotingRule;
import org.upmc.electisim.OmniscientBestResponseStrategy;
import org.upmc.electisim.PreferenceType;
import org.upmc.electisim.SimulationProfile;

public class SimulationProfileFileReader extends ASimulationProfileReader {

	JSONTokener jsonTokener;
	
	public SimulationProfileFileReader(String filename) throws FileNotFoundException {
		this(new File(filename));		
	}
	
	public SimulationProfileFileReader(File file) throws FileNotFoundException {
		super(file);
		jsonTokener = new JSONTokener(inputStream);
	}

	@Override
	public SimulationProfile loadProfile() {
		
		//TODO 19.04.2018 : store object (must implement Serializable) in the profilefilewriter and then retrieve it here
		
		JSONObject root = new JSONObject(jsonTokener);
		
		String json_prefType = root.getString("prefType");
		String json_votingRule = root.getString("votingRule");
		String json_agentStrategy = root.getString("agentStrategy");
		JSONArray json_candidates = root.getJSONArray("candidateList");
		JSONArray json_agents = root.getJSONArray("agentList");
		
		PreferenceType prefType;
		IVotingRule votingRule = null; // TODO 19.04.2018 : doesn't look good
		IAgentStrategy agentStrategy;
		List<Agent> agentList;
		List<Candidate> candidateList;
		
		//Load preferenceType
		switch(json_prefType){
		case "HAMMING" : prefType = PreferenceType.HAMMING;
		break;
		case "RESPONSIVE" : prefType = PreferenceType.RESPONSIVE;
		break;
		}

		//Load votingRule
		switch(json_votingRule){
		case "BlocVotingRule" : votingRule = new BlocVotingRule();
		break;
		}
		
		//Load agentStrategy
		switch(json_agentStrategy){
		case "OmniscientBestResponseStrategy" : agentStrategy = new OmniscientBestResponseStrategy(null, votingRule); // TODO 19.04.2018 : doesn't look good neither 
		}
		
		/*
		//Load candidateList
		for(int i=0; i<json_candidates.size(); i++){
			
		}*/
		
		
		
		return null;
	}

}
