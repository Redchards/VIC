package org.upmc.electisim.output;

import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;

import org.apache.commons.io.FilenameUtils;
import org.upmc.electisim.SimulationProfile;

public class SimulationProfileObjectWriter extends ASimulationProfileWriter {

	private ObjectOutputStream oos;
	public static final String DEFAULT_PROFILE_FILENAME = "Profile_config.txt";
	
	public SimulationProfileObjectWriter() throws IOException, InvalidExtensionException{
		super(DEFAULT_PROFILE_FILENAME);
		initObjectOutputStream(FilenameUtils.getExtension(DEFAULT_PROFILE_FILENAME));
	}
	
	public SimulationProfileObjectWriter(String filename) throws InvalidExtensionException, IOException {
		super((FilenameUtils.getExtension(filename).isEmpty()) ? 
				filename+".txt" : filename);
		
		initObjectOutputStream(FilenameUtils.getExtension(filename));
	}

	
	public SimulationProfileObjectWriter(File file) throws InvalidExtensionException, IOException {
		super((FilenameUtils.getExtension(file.getName()).isEmpty()) ? 
				file : file);
		
		initObjectOutputStream(FilenameUtils.getExtension(file.getName()));
	}
	
	private void initObjectOutputStream(String extension) throws IOException, InvalidExtensionException {
		if(extension.isEmpty() || extension.equals("txt"))
		{
			oos = new ObjectOutputStream(underlyingStream);
		}
		else
		{
			throw new InvalidExtensionException(extension, "txt");
		}
	}

	@Override
	public void writeProfile(SimulationProfile profile) throws IOException {
		oos.writeObject(profile);
		oos.flush();
	}

	public void close() throws IOException{
		oos.close();
		super.close();
	}

}
