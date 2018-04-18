package org.upmc.electisim.output;

import java.io.File;
import java.io.FileNotFoundException;

import org.upmc.electisim.SimulationState;

public class StateFileWriter extends AStateWriter {

	
	//Delimiter used in CSV file
	    private static final String COMMA_DELIMITER = ",";

	    private static final String NEW_LINE_SEPARATOR = "\n";
	    //CSV file header
	    private static final String FILE_HEADER = "id,firstName,age";

	
	
	public StateFileWriter(String filename) throws FileNotFoundException {
		super(filename);
	}

	public StateFileWriter(File file) throws FileNotFoundException {
		super(file);
	}

	/////////////
	public void writeState(SimulationState state){
		
		state.getVoteResults();
		StringBuilder sb = new StringBuilder();
		sb.append(FILE_HEADER.toString());
		sb.append(NEW_LINE_SEPARATOR);
		sb.append("01");
		sb.append(COMMA_DELIMITER);
		sb.append("Yasmiiine>Winnie>Bobby");
		sb.append(COMMA_DELIMITER);
		sb.append("14");
		sb.append(NEW_LINE_SEPARATOR);
		
		sb.append("02");
		sb.append(COMMA_DELIMITER);
		sb.append("Bibibidi");
		sb.append(COMMA_DELIMITER);
		sb.append("25");
		sb.append(NEW_LINE_SEPARATOR);
		
		//super.writeState(sb.toString());
		super.write(sb.toString());
	}
}
