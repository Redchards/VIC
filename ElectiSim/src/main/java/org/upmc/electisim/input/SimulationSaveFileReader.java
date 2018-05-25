package org.upmc.electisim.input;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.upmc.electisim.Agent;
import org.upmc.electisim.Candidate;
import org.upmc.electisim.IElectable;
import org.upmc.electisim.IAgentStrategy;
import org.upmc.electisim.IVotingRule;
import org.upmc.electisim.PreferenceType;
import org.upmc.electisim.Preferences;
import org.upmc.electisim.SimulationProfile;
import org.upmc.electisim.output.InvalidExtensionException;

/**
 * <p>A reader aiming to read a {@link org.upmc.electisim.SimulationProfile} from a JSON file.</p>
 * <p>Here is a brief rundown of the JSON format used (the elements enclosed inside of two 
 * "$" signs are variables) :</p>
 * <pre>
 * {
 *   "prefType" : $PREFERENCE_TYPE$ (string),
 *   "committee_size": $COMMITTEE_SIZE$ (int),
 *   "votingRule": $VOTING_RULE_CLASS_NAME$ (string),
 *   "agentStrategy": $AGENT_STRATEGY_CLASS_NAME$ (string),
 *   "candidateList": [
 *     {"cdt_name": $NAME$ (string)},
 *     ...
 *     {"cdt_name": $NAME$ (string)}
 *   ]
 *   "agentList": [
 *     {
 *       "preferences": [
 *         {"cdt_name": $NAME$ (string)},
 *         ...
 *         {"cdt_name": $NAME$ (string)}
 *       ],
 *       "agt_name": $NAME$ (string)
 *     },
 *     ...
 *   ]
 * }</pre>
 *  
 * @see org.upmc.electisim.output.SimulationSaveFileWriter
 */
public class SimulationSaveFileReader extends ASimulationSaveReader {

	/*
	 * (non-Javadoc)
	 * The JSON tokenizer used to read thhe JSON file
	 */
	JSONTokener jsonTokener;
	
	/**
	 * Build a reader using a filename
	 * 
	 * @param filename the name of the file to read
	 * @throws IOException if the file is not found or can't be read
	 * @throws InvalidExtensionException if the extention of the file is invalid
	 */
	public SimulationSaveFileReader(String filename) throws IOException, InvalidExtensionException {
		super(new FileInputStream(filename));
		initJSONTokener(FilenameUtils.getExtension(filename));
		
	}
	
	/**
	 * Build a reader using a file
	 * 
	 * @param file the file to read
	 * @throws IOException if the file is not found or can't be read
	 * @throws InvalidExtensionException if the extention of the file is invalid
	 */
	public SimulationSaveFileReader(File file) throws IOException, InvalidExtensionException {
		super(new FileInputStream(file));
		initJSONTokener(FilenameUtils.getExtension(file.getName()));
		
	}

	/*
	 * (non-Javadoc)
	 * Init the JSON tokenizer
	 * 
	 * @param extension gives the extension of the file
	 * @throws IOException
	 * @throws InvalidExtensionException
	 */
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

	/*
	 * (non-Javadoc)
	 * Load the simulation profile
	 * 
	 * @see org.upmc.electisim.input.ASimulationSaveReader#loadProfile()
	 */
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
				
				
				//Load agentStrategy
				IAgentStrategy agentStrategy = (IAgentStrategy) Class.forName(json_agentStrategy).newInstance();;
				
				Map<String, IElectable> candidateMap = new HashMap<>();

				//Load candidateList
				for(int i=0; i<json_candidateList.length(); i++){
					JSONObject json_candidate = json_candidateList.getJSONObject(i);
					String candidateName = json_candidate.getString("cdt_name");
					candidateMap.put(candidateName, new Candidate(candidateName));
				}
				
				List<IElectable> candidateList = new ArrayList<>(candidateMap.values());

				//Load votingRule
				IVotingRule votingRule = (IVotingRule) Class.forName(json_votingRule).newInstance();
				
				//Load agentList
				List<Agent> agentList = new ArrayList<Agent>();
				for(int i=0; i<json_agentList.length(); i++){
					JSONObject json_agent = json_agentList.getJSONObject(i);
					String agentName = json_agent.getString("agt_name");
					
					//Load agent preferences
					JSONArray json_preferences = json_agent.getJSONArray("preferences");
					List<IElectable> prefList = new ArrayList<IElectable>();
					for(int j=0; j<json_preferences.length(); j++){
						JSONObject json_candidate = json_preferences.getJSONObject(j);
						prefList.add(candidateMap.get(json_candidate.getString("cdt_name")));
					}
					
					agentList.add(new Agent(agentName, new Preferences(prefType, prefList)));
					
				}
				
				int committeeSize = root.getInt("committee_size");
				
				return new SimulationProfile(prefType, votingRule, agentStrategy, agentList, candidateList, committeeSize);
				
	}
	

}
