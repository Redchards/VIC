package org.upmc.electisim.output;

import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.io.FilenameUtils;
import org.upmc.electisim.AgentVote;
import org.upmc.electisim.ElectionResult;
import org.upmc.electisim.IElectable;
import org.upmc.electisim.SimulationState;

public class StateFileWriter extends AStateWriter{

	CSVPrinter csvPrinter;


	public StateFileWriter(String filename) throws IOException, InvalidExtensionException {
		this(filename, CSVFormat.DEFAULT);  
		
	}

	public  StateFileWriter(File file) throws IOException, InvalidExtensionException{
		this(file, CSVFormat.DEFAULT);		
	}
	
	public StateFileWriter(String filename, CSVFormat csvFormat) throws IOException, InvalidExtensionException{
		super((FilenameUtils.getExtension(filename).isEmpty()) ? 
				filename+".csv" : filename);	
		
		initCSVPrinter(FilenameUtils.getExtension(filename), csvFormat);
		
	}
	
	public StateFileWriter(File file, CSVFormat csvFormat) throws IOException, InvalidExtensionException{
			
		super((FilenameUtils.getExtension(file.getName()).isEmpty()) ? 
				new File(file.getName()+".csv") : file);  
		
		initCSVPrinter(FilenameUtils.getExtension(file.getName()), csvFormat);
	}
	
	
	
	private void initCSVPrinter(String extension, CSVFormat csvFormat) throws IOException, InvalidExtensionException {
		if(extension.isEmpty() || extension.equals("csv"))
		{
			this.csvPrinter = new CSVPrinter(new OutputStreamWriter(underlyingStream), csvFormat);
		}
		else
		{
			throw new InvalidExtensionException(extension, "csv");
			
		}
	}

	
	public void writeState(SimulationState state) throws IOException {
		
		List<AgentVote> results = state.getVoteResults();
		List<IElectable> candidateList = state.getProfile().getCandidateList();
				

		//CSV Header
		csvPrinter.print("Agents\\Candidates");
		for(IElectable candidate : candidateList){
			csvPrinter.print(candidate.getName());
		}
		
		//or
//		csvPrinter.print("Candidates or Committees");
//		Iterator electableIterator = state.getElectionResult().candidateIterator();
//		while(electableIterator.hasNext()){
//			csvPrinter.print(((IElectable)electableIterator.next()).getName());
//		}
		
		

		//Votes
		for(AgentVote vote : results){
			csvPrinter.println();
			csvPrinter.print(vote.getAgent().getName());
			Map<IElectable, Integer> scoreMap = vote.getScoreMap();			
			for(IElectable candidate : candidateList){
				csvPrinter.print(scoreMap.get(candidate));
			}		
		}

		
		ElectionResult electionResult = state.getElectionResult();
		
		//Scores
		csvPrinter.println();
		csvPrinter.println();
		csvPrinter.print("Final Scores");
		csvPrinter.println();
//		for(IElectable candidate : candidateList){
//			csvPrinter.print(electionResult.getCandidateScore(candidate));
//		}	
		Iterator electableIterator = state.getElectionResult().candidateIterator();
		while(electableIterator.hasNext()){
			//IElectable electable = (IElectable)electableIterator.next();
			csvPrinter.print(((IElectable)electableIterator.next()).getName());
		}
		csvPrinter.println();
		csvPrinter.println();
		electableIterator = state.getElectionResult().candidateIterator();
		while(electableIterator.hasNext()){
			IElectable electable = (IElectable)electableIterator.next();
			csvPrinter.print(electionResult.getCandidateScore(electable));
		}
		
		
		//Elected committee 
		csvPrinter.println();
		csvPrinter.println();
		csvPrinter.print("Elected Committee");
		for(IElectable candidate : electionResult.getElectedCommittee()){
			csvPrinter.print(candidate.getName());
		}
		

	}

	
	public void close() throws IOException{
		csvPrinter.close();
		super.close();
	}


}
