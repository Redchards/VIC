package org.upmc.electisim.output;

import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.io.FilenameUtils;
import org.upmc.electisim.Candidate;
import org.upmc.electisim.SimulationState;
import org.upmc.electisim.VoteResult;

public class StateFileWriter extends AStateWriter {

	CSVPrinter csvPrinter;


	public StateFileWriter(String filename) throws IOException {
		this(filename, CSVFormat.DEFAULT);  
		
	}

	public  StateFileWriter(File file) throws IOException{
		this(file, CSVFormat.DEFAULT);		
	}
	
	public StateFileWriter(String filename, CSVFormat csvFormat) throws IOException{
		this((FilenameUtils.getExtension(filename).isEmpty()) ? 
				new File(filename+".csv") : (!FilenameUtils.getExtension(filename).equals(".csv")) ? 
						new File(filename.subSequence(0, filename.lastIndexOf('.'))+".csv") : new File(filename),
						csvFormat);
	}
	
	public StateFileWriter(File file, CSVFormat csvFormat) throws IOException{
			
		super((FilenameUtils.getExtension(file.getName()).isEmpty()) ? 
				new File(file.getName()+".csv") : (!FilenameUtils.getExtension(file.getName()).equals(".csv")) ? 
					new File(file.getName().subSequence(0, file.getName().lastIndexOf('.'))+".csv") : file);  
		
		this.csvPrinter = new CSVPrinter(new OutputStreamWriter(underlyingStream), csvFormat);
		
		if(!FilenameUtils.getExtension(file.getName()).equals(".csv")){
			file.delete();
		}
	}
	
	
	
	public void writeState(SimulationState state) throws IOException, InvalidStateException {
		
		//TODO 19.04.2018 : check if not empty else throw new exception ?
		if(state == null || state.getVoteResults() == null || state.getVoteResults().isEmpty())
		{
			throw new InvalidStateException();
		}
		
		List<VoteResult> results = state.getVoteResults();
	
		//CSV Header
		Map<Candidate, Integer> scoreMap = results.get(0).getScoreMap();
		csvPrinter.print("Agents\\Candidates");
		for(Candidate candidate : scoreMap.keySet()){
			csvPrinter.print(candidate.getName());
		}		



		//Results
		for(VoteResult vote : results){
			csvPrinter.println();
			csvPrinter.print(vote.getAgent().getName());
			scoreMap = vote.getScoreMap();			
			for(Candidate candidate : scoreMap.keySet()){
				csvPrinter.print(scoreMap.get(candidate));
			}		
		}

	}

	public void close() throws IOException{
		csvPrinter.close();
		super.close();
	}


}
