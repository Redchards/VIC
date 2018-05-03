package org.upmc.electisim.input;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.upmc.electisim.Agent;
import org.upmc.electisim.Candidate;
import org.upmc.electisim.IAgentStrategy;
import org.upmc.electisim.IVotingRule;
import org.upmc.electisim.PreferenceType;
import org.upmc.electisim.Preferences;
import org.upmc.electisim.SimulationProfile;
import org.upmc.electisim.output.InvalidExtensionException;

public class SimulationSaveFileReader extends ASimulationSaveReader {

	JSONTokener jsonTokener;
	
	public SimulationSaveFileReader(String filename) throws IOException, InvalidExtensionException {
		super(filename);
		initJSONTokener(FilenameUtils.getExtension(filename));
		
	}
	
	public SimulationSaveFileReader(File file) throws IOException, InvalidExtensionException {
		super(file);
		initJSONTokener(FilenameUtils.getExtension(file.getName()));
		
	}

	private void initJSONTokener(String extension) throws IOException, InvalidExtensionException {
		if(extension.equals("json"))
		{
			jsonTokener = new JSONTokener(underlyingStream);
		}
		else
		{
			throw new InvalidExtensionException(extension, "json");
		}
		
	}

	@Override
	public SimulationProfile loadProfile() throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		
		JSONObject root = new JSONObject(jsonTokener);
				
				String json_prefType = root.getString("prefType");
				String json_votingRule = root.getString("votingRule");
				String json_agentStrategy = root.getString("agentStrategy");
				JSONArray json_candidateList = root.getJSONArray("candidateList");
				JSONArray json_agentList = root.getJSONArray("agentList");
				
				// TODO prefCompleter + better way to deserialize prefType
				
				//Load preferenceType
				PreferenceType prefType = null;
				switch(json_prefType){
				case "HAMMING" : prefType = PreferenceType.HAMMING;
				break;
				case "RESPONSIVE" : prefType = PreferenceType.RESPONSIVE;
				break;
				}
				
				//Load votingRule
				IVotingRule votingRule = (IVotingRule) Class.forName(json_votingRule).newInstance();
				
				//Load agentStrategy
				IAgentStrategy agentStrategy = (IAgentStrategy) Class.forName(json_agentStrategy).newInstance();;
				
				Map<String, Candidate> candidateMap = new HashMap<>();

				//Load candidateList
				for(int i=0; i<json_candidateList.length(); i++){
					JSONObject json_candidate = json_candidateList.getJSONObject(i);
					String candidateName = json_candidate.getString("cdt_name");
					candidateMap.put(candidateName, new Candidate(candidateName));
				}
				
				List<Candidate> candidateList = new ArrayList<>(candidateMap.values());

				
				//Load agentList
				List<Agent> agentList = new ArrayList<Agent>();
				for(int i=0; i<json_agentList.length(); i++){
					JSONObject json_agent = json_agentList.getJSONObject(i);
					String agentName = json_agent.getString("agt_name");
					
					//Load agent preferences
					JSONArray json_preferences = json_agent.getJSONArray("preferences");
					List<Candidate> prefList = new ArrayList<Candidate>();
					for(int j=0; j<json_preferences.length(); j++){
						JSONObject json_candidate = json_preferences.getJSONObject(j);
						prefList.add(candidateMap.get(json_candidate.getString("cdt_name")));
					}
					
					agentList.add(new Agent(agentName, new Preferences(prefType, prefList)));
					
				}
				
				return new SimulationProfile(prefType, votingRule, agentStrategy, agentList, candidateList);
				
	}
	

}
