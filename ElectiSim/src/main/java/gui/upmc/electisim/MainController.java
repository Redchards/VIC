package gui.upmc.electisim;

import java.io.File;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.function.Function;
import java.util.function.UnaryOperator;

import org.upmc.electisim.SimulationEngine;
import org.upmc.electisim.SimulationProfile;
import org.upmc.electisim.input.SimulationSaveFileReader;
import org.upmc.electisim.output.InvalidExtensionException;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.stage.Window;

public class MainController {

	@FXML
	private TextField iterationCountTextField;
	
	@FXML
	private TextField bufferSizeTextField;
	
	@FXML
	private TextField committeeSizeTextField;
	
	@FXML
	private TextField timestepTextField;
	
	@FXML
	private Button stepBackButton;
	
	@FXML
	private Button runBackwardButton;
	
	@FXML
	private Button runButton;
	
	@FXML
	private Button stepButton;
	
	@FXML
	private Button pauseButton;
	
	@FXML
	private Button stopButton;
	
	@FXML
	private Button saveButton;
	
	@FXML
	private BarChart<Number, Number> resultGraph;
	
	@FXML
	private Label electedCommitteeLabel;
	
	@FXML
	private MenuItem loadProfileMenu;
	
	private SimulationEngine simulationEngine;
	
	private Scene scene;
	
	
	@FXML
	void initialize() {
		simulationEngine = null;		
		
		NumberFormat numberFormat = NumberFormat.getInstance();
		UnaryOperator<TextFormatter.Change> formatOperator = c ->
		{
		    if ( c.getControlNewText().isEmpty() )
		    {
		        return c;
		    }

		    ParsePosition parsePosition = new ParsePosition( 0 );
		    Object object = numberFormat.parse( c.getControlNewText(), parsePosition );

		    if ( object == null || parsePosition.getIndex() < c.getControlNewText().length() )
		    {
		        return null;
		    }
		    else
		    {
		        return c;
		    }
		};
		
		this.committeeSizeTextField.setTextFormatter(new TextFormatter<>(formatOperator));
		this.iterationCountTextField.setTextFormatter(new TextFormatter<>(formatOperator));
		this.bufferSizeTextField.setTextFormatter(new TextFormatter<>(formatOperator));
		this.timestepTextField.setTextFormatter(new TextFormatter<>(formatOperator));
		
		
		
		loadProfileMenu.setOnAction((action) -> {
			Window stage = scene.getWindow();
			
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Open Resource File");
			fileChooser.getExtensionFilters().addAll(
		         new ExtensionFilter("Json Files", "*.json"));
            fileChooser.setInitialDirectory(new File(System.getProperty("user.dir")));
			File selectedFile = fileChooser.showOpenDialog(stage);
			if (selectedFile != null) {
				
				try(SimulationSaveFileReader reader = new SimulationSaveFileReader(selectedFile)) {
					SimulationProfile profile = reader.loadProfile();
					
					int committeeSize = 0;
					if(!committeeSizeTextField.getText().isEmpty()) {
						committeeSize = Integer.parseInt(committeeSizeTextField.getText());
					}
					
					this.simulationEngine = new SimulationEngine(profile, committeeSize);

				} catch (Exception e) {
                    Platform.runLater(() -> displayError("Configuration loading failed", e.getMessage()));
				} 
			}
		 });
		
		System.out.println("Hello");
	}
	
	public void setScene(Scene scene) {
		this.scene = scene;
	}
	
    private void displayError(String heading, String content) {

        Alert alert = new Alert(AlertType.ERROR);

        alert.setTitle("Error");
        alert.setHeaderText(heading);
        alert.setContentText(content);

        alert.showAndWait();

    }

}
