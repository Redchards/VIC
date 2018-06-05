package gui.upmc.electisim;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.upmc.electisim.Candidate;
import org.upmc.electisim.IElectable;

import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class BarChartViewConfigBox {

	private Stage newStage;
		
	public BarChartViewConfigBox(BarChartView view) {
		newStage = new Stage();
		
		
		VBox root = new VBox();
		Label name = new Label("Number of series");
		TextField numberField = new TextField("NumberOfSeries");
		
		root.getChildren().add(numberField);
		
		numberField.setText(Integer.toString(view.getNumberOfSeries()));
		numberField.setTextFormatter(FormatterProvider.getNumberFormatter());
		
		Scene stageScene = new Scene(root);
		newStage.setScene(stageScene);
		newStage.setOnHiding((evt) -> {
			if(!numberField.getText().isEmpty()) {
				view.setNumberOfSeries(Integer.parseInt(numberField.getText()));
			}
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
	
}
