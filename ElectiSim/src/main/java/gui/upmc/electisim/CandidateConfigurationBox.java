package gui.upmc.electisim;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.upmc.electisim.Candidate;
import org.upmc.electisim.IElectable;

import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class CandidateConfigurationBox {

	private IElectable newCandidate;
	private Stage newStage;
	
	private CandidateConfigurationException bufferedException;
	
	public CandidateConfigurationBox(IElectable candidate, List<IElectable> candidateList) {
		newCandidate = null;
		newStage = new Stage();
		
		bufferedException = null;
		
		VBox root = new VBox();
		TextField nameField = new TextField("Name");
		
		root.getChildren().add(nameField);
		
		nameField.setText(candidate.getName());
		
		Scene stageScene = new Scene(root);
		newStage.setScene(stageScene);
		newStage.setOnHiding((evt) -> {
			newCandidate = new Candidate(nameField.getText());
			
			for(IElectable c : candidateList) {
				if(c.getName().equals(newCandidate.getName())) {
					bufferedException = new CandidateConfigurationException(newCandidate, "the candidate is a duplicate !");
					newCandidate = null;
					break;
				}
			}
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
	
	public Optional<IElectable> getCandidate() throws CandidateConfigurationException {
		if(bufferedException != null) {
			throw bufferedException;
		}
		return Optional.ofNullable(newCandidate);
	}
	
}
