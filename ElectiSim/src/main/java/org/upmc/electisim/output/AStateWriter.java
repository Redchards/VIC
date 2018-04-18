package org.upmc.electisim.output;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.upmc.electisim.Candidate;
import org.upmc.electisim.SimulationState;
import org.upmc.electisim.VoteResult;

public abstract class AStateWriter extends AGenericWriter {

	private OutputStreamWriter outputStreamWriter;
	private CSVPrinter csvPrinter;
	
	
	public AStateWriter(String filename) throws FileNotFoundException {
		super(filename);
		this.outputStreamWriter = new OutputStreamWriter(super.getOutputStream()); //TODO : Ask about this
		
		try {
			
			this.csvPrinter = new CSVPrinter(outputStreamWriter, CSVFormat.DEFAULT);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public AStateWriter(File file) throws FileNotFoundException {
		super(file);
	}

	protected AStateWriter(OutputStream stream) {
		super(stream);
	}
	
	public void writeState(SimulationState state) throws IOException{
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
		outputStreamWriter.close();
		super.close();
	}

	
	
}
