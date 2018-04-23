package org.upmc.electisim.input;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;

import org.apache.commons.io.FilenameUtils;
import org.upmc.electisim.SimulationProfile;
import org.upmc.electisim.output.InvalidExtensionException;
import org.upmc.electisim.output.SimulationProfileObjectWriter;

public class SimulationProfileObjectReader extends ASimulationProfileReader {

	private ObjectInputStream ois;
	
	
	public SimulationProfileObjectReader() throws IOException, InvalidExtensionException {
		super(SimulationProfileObjectWriter.DEFAULT_PROFILE_FILENAME);
		initObjectInputStream(FilenameUtils.getExtension(SimulationProfileObjectWriter.DEFAULT_PROFILE_FILENAME));
		
	}
	
	public SimulationProfileObjectReader(String filename) throws IOException, InvalidExtensionException {
		super(filename);
		initObjectInputStream(FilenameUtils.getExtension(filename));
		
	}
	
	public SimulationProfileObjectReader(File file) throws IOException, InvalidExtensionException {
		super(file);
		initObjectInputStream(FilenameUtils.getExtension(file.getName()));
		
	}

	private void initObjectInputStream(String extension) throws IOException, InvalidExtensionException {
		if(extension.equals("txt"))
		{
			ois = new ObjectInputStream(inputStream);
		}
		else
		{
			throw new InvalidExtensionException(extension, "txt");
		}
		
	}

	@Override
	public SimulationProfile loadProfile() throws ClassNotFoundException, IOException {
		return (SimulationProfile) ois.readObject();
	}
	
	public void close() throws IOException{
		ois.close();
		super.close();
	}

}
