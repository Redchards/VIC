package org.upmc.electisim.output;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.OutputStream;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.upmc.electisim.Agent;
import org.upmc.electisim.Candidate;
import org.upmc.electisim.SimulationProfile;

public abstract class ASimulationProfileWriter extends AGenericWriter {

	public ASimulationProfileWriter(String filename) throws FileNotFoundException {
		super(filename);
	}

	public ASimulationProfileWriter(File file) throws FileNotFoundException {
		super(file);
	}

	protected ASimulationProfileWriter(OutputStream stream) {
		super(stream);
	}
	
	public void writeProfile(SimulationProfile profile){
		
		String json_content = convertToJSON(profile); 	
		super.write(json_content);
		
        
	}

	private String convertToJSON(SimulationProfile profile) {
		JSONObject main = new JSONObject();
		
		//add PrefType 
		main.put("prefType", profile.getPreferenceType().toString());
		
		//add VotingRule
		String[] votingRule = profile.getVotingRule().getClass().toString().split("\\.");
		main.put("votingRule", votingRule[votingRule.length-1]);
		
		//add AgentStrategy
		String[] agentStrategy = profile.getVotingStrategy().getClass().toString().split("\\.");
        main.put("agentStrategy", agentStrategy[agentStrategy.length-1]);
        
        //add agents
        JSONArray json_agentList = new JSONArray();
        
        for(Agent agent : profile.getAgentList()){        	
            
        	JSONObject json_agent = new JSONObject();
        	
        	//add agent name
            json_agent.put("agt_name", agent.getName());
            
            //add preferences of the agent
            List<Candidate> prefList = agent.getPreferences().getPreferenceList();
            JSONArray json_prefList = new JSONArray();
            
            for(Candidate candidate : prefList){
            	JSONObject json_candidate = new JSONObject();
                json_candidate.put("cdt_name", candidate.getName());
                json_prefList.put(json_candidate);
            }
            
            json_agent.put("preferences", json_prefList);
            
            //add agent to the agentList
            json_agentList.put(json_agent);
        }
        main.put("agentList", json_agentList);

        //add candidates
        JSONArray json_candidateList = new JSONArray();
        for(Candidate candidate : profile.getCandidateList()){
            JSONObject json_candidate = new JSONObject();
            json_candidate.put("cdt_name", candidate.getName());
            json_candidateList.put(json_candidate);
        }
        main.put("candidateList", json_candidateList);
        
        return main.toString(4);

	}

}
