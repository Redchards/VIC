package org.upmc.electisim.output;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.upmc.electisim.Agent;
import org.upmc.electisim.IElectable;
import org.upmc.electisim.SimulationProfile;

/**
 * <p>A writer aiming to writer a {@link org.upmc.electisim.SimulationProfile} from a JSON file.</p>
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
 * @see org.upmc.electisim.input.SimulationSaveFileReader
 */
public class SimulationSaveFileWriter extends ASimulationSaveWriter {



	public SimulationSaveFileWriter(String filename) throws FileNotFoundException, InvalidExtensionException {
		super((FilenameUtils.getExtension(filename).isEmpty()) ? 
				filename+".json" : filename);
		
		checkExtensionValidity(FilenameUtils.getExtension(filename));
	}

	public SimulationSaveFileWriter(File file) throws FileNotFoundException, InvalidExtensionException {
		super((FilenameUtils.getExtension(file.getName()).isEmpty()) ? 
				new File(file.getName()+".json") : file); 
		
		checkExtensionValidity(FilenameUtils.getExtension(file.getName()));
				
	}

	private void checkExtensionValidity(String extension) throws InvalidExtensionException {
		if(!extension.isEmpty() && !extension.equals("json"))
		{
			
			throw new InvalidExtensionException(extension, "json");
		}		
	}
	
	public void writeProfile(SimulationProfile profile) throws IOException{

		String json_content = convertToJSON(profile); 	
		write(json_content.getBytes());
	}

	
	private String convertToJSON(SimulationProfile profile) {
		JSONObject root = new JSONObject();

		//add PrefType 
		root.put("prefType", profile.getPreferenceType().toString());

		//add VotingRule
		root.put("votingRule", profile.getVotingRule().getClass().getName());

		//add AgentStrategy
		root.put("agentStrategy", profile.getVotingStrategy().getClass().getName());

		//add Agents
		JSONArray json_agentList = new JSONArray();

		for(Agent agent : profile.getAgentList()){        	

			JSONObject json_agent = new JSONObject();

			//add agent name
			json_agent.put("agt_name", agent.getName());

			//add preferences of the agent
			List<IElectable> prefList = agent.getPreferences().getPreferenceList();
			JSONArray json_prefList = new JSONArray();

			for(IElectable candidate : prefList){
				JSONObject json_candidate = new JSONObject();
				json_candidate.put("cdt_name", candidate.getName());
				json_prefList.put(json_candidate);
			}

			json_agent.put("preferences", json_prefList);

			//add agent to the agentList
			json_agentList.put(json_agent);
		}
		root.put("agentList", json_agentList);

		//add Candidates
		JSONArray json_candidateList = new JSONArray();
		for(IElectable candidate : profile.getCandidateList()){
			JSONObject json_candidate = new JSONObject();
			json_candidate.put("cdt_name", candidate.getName());
			json_candidateList.put(json_candidate);
		}
		root.put("candidateList", json_candidateList);
		
		root.put("committee_size", profile.getCommitteeSize());

		return root.toString(4);

	}

}
