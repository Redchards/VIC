package gui.upmc.electisim;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.upmc.electisim.Agent;
import org.upmc.electisim.IElectable;
import org.upmc.electisim.PreferenceType;
import org.upmc.electisim.Preferences;

import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AgentConfigurationBox {

	private Agent newAgent;
	private Stage newStage;
	
	private AgentConfigurationException bufferedException;
	
	public AgentConfigurationBox(Agent agent, PreferenceType type, List<IElectable> candidateList) {
		newAgent = null;
		bufferedException = null;
		newStage = new Stage();
		
		VBox root = new VBox();
		
		TextField nameField = new TextField("Name");
		TextField preferences = new TextField("Preferences");
		
		root.getChildren().add(nameField);
		root.getChildren().add(preferences);
		
		nameField.setText(agent.getName());
		String stringizedPrefList = agent.getPreferences().getPreferenceList().toString();
		preferences.setText(stringizedPrefList.substring(1, stringizedPrefList.length() - 1));

		Scene stageScene = new Scene(root);
		newStage.setScene(stageScene);
		newStage.setOnHiding((evt) -> {
			List<String> prefListStr = Arrays.asList(preferences.getText().split(","));
			
			List<String> candidateListStr = new ArrayList<>();
			
			for(IElectable c : candidateList) {
				candidateListStr.add(c.getName());
			}
			
			List<IElectable> prefList = new ArrayList<>();
			
			for(String candidateName : prefListStr) {
				int candidateIdx = candidateListStr.indexOf(candidateName.trim());
				
				if(candidateIdx != -1) {
					IElectable c = candidateList.get(candidateIdx);
					if(!prefList.contains(c)) {
						prefList.add(c);
					}
					else {
						bufferedException = new AgentConfigurationException(agent, "the candidate '" + candidateName + "' is a duplicate !");
					}
				}
				else {
					bufferedException = new AgentConfigurationException(agent, "the candidate '" + candidateName + "' doesn't exist !");
				}
			}
			newAgent = new Agent(nameField.getText(), new Preferences(type, prefList));
			//Stage stage = (Stage)evt.getSource();
			// ObservableList<Node> childrenList = stage.getScene().getRoot().getChildrenUnmodifiable();
		});
		
		root.addEventHandler(KeyEvent.KEY_PRESSED, ev -> {
	        if (ev.getCode() == KeyCode.ENTER) {
	           newStage.close();
	        }
	    });
	}
	
	public void showAndWait() {
		newStage.initModality(Modality.APPLICATION_MODAL);
		newStage.showAndWait();
	}
	
	public Optional<Agent> getAgent() throws AgentConfigurationException {
		if(bufferedException != null) {
			throw bufferedException;
		}
		
		return Optional.ofNullable(newAgent);
	}
	
}
