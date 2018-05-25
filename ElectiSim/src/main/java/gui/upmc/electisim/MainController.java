package gui.upmc.electisim;

import java.io.File;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.upmc.electisim.Agent;
import org.upmc.electisim.IElectable;
import org.upmc.electisim.ElectionResult;
import org.upmc.electisim.SimulationEngine;
import org.upmc.electisim.SimulationProfile;
import org.upmc.electisim.input.SimulationSaveFileReader;
import org.upmc.electisim.output.InvalidExtensionException;
import org.upmc.electisim.utils.SimulationEngineConfigDefaults;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.scene.chart.XYChart;

public class MainController {

	@FXML
	private TextField iterationCountTextField;
	
	@FXML
	private TextField bufferSizeTextField;
	
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
	private Label electedCommitteeLabel;
	
	@FXML
	private MenuItem loadProfileMenu;
	
	@FXML
	private Button editProfileButton;
	
	@FXML
	private AnchorPane viewPane;
	
	private BarChartView barChartView;
	
	private SimulationEngine simulationEngine;
	
	private Scene scene;
		
	
	@FXML
	void initialize() {
		simulationEngine = null;
		
		// this.resultGraph.setTitle("Results");
		this.barChartView = new BarChartView(viewPane, "Candidates", "Scores", 5);
		
		
		this.iterationCountTextField.setTextFormatter(FormatterProvider.getNumberFormatter());
		this.bufferSizeTextField.setTextFormatter(FormatterProvider.getNumberFormatter());
		this.timestepTextField.setTextFormatter(FormatterProvider.getNumberFormatter());
		
		
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
					
					
					for(Agent a : profile.getAgentList()) {
						System.out.println(a.getPreferences().getPreferenceList().toString());
						System.out.println(profile.getCandidateList().get(1).toString());
						System.out.println(a.getPreferences().getCandidateDistance(profile.getCandidateList().get(1)));
					}
					
					System.out.println(profile.getAgentList());
					System.out.println(profile.getCandidateList());
					System.out.println(profile.getPreferenceType());
					System.out.println(profile.getVotingRule());
					System.out.println(profile.getVotingStrategy());
					
					loadNewEngine(profile);

				} catch (Exception e) {
                    Platform.runLater(() -> DialogBoxHelper.displayError("Configuration loading failed", e.getMessage()));
				} 
			}
		 });
		
		this.runButton.setOnAction(action -> {
			if(this.simulationEngine != null && !this.simulationEngine.isRunning()) {
				Thread runner = new Thread(() -> {
					try {
						this.simulationEngine.run();
					} catch (InterruptedException e) {
						Platform.runLater(() -> DialogBoxHelper.displayError("Error during the simulation", e.getMessage()));
					}
				});
				runner.setDaemon(true);
				runner.start();
			}
			else if(this.simulationEngine == null) {
                Platform.runLater(() -> DialogBoxHelper.displayInfo("Unable to launch simulation", "No simulation profile loaded"));
			}
		});
		
		this.pauseButton.setOnAction(action -> {
			if(this.simulationEngine != null) {
				this.simulationEngine.pause();
			}
		});
		
		this.stepButton.setOnAction(action -> {
			if(this.simulationEngine != null) {
				this.simulationEngine.pause();
				this.simulationEngine.step();
			}
		});
		
		this.editProfileButton.setOnAction(action -> {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/graphics/profile_conf.fxml"));
			try {
				Parent root = loader.load();
				ProfileConfigurationController controller = loader.getController();
				
				if(this.simulationEngine != null) {
					controller.setSimulationProfile(this.simulationEngine.getSimulationProfile());
				}
				
				Stage stage = new Stage();
				stage.setScene(new Scene(root));
				
				stage.setOnHiding((evt) -> {
					SimulationProfile profile;
					try {
						profile = controller.buildSimulationProfile();
						
						loadNewEngine(profile);
					} catch (SimulationProfileConfigurationException e) {
						Platform.runLater(() -> DialogBoxHelper.displayWarning("Unable to update the configuration", e.getMessage()));
					}
					
				});
				
				stage.initModality(Modality.APPLICATION_MODAL);
				stage.showAndWait();
				
			} catch (IOException e) {
				Platform.runLater(() -> DialogBoxHelper.displayError("Error when loading GUI", e.getMessage()));
			}

		});
	}
	
	public void setScene(Scene scene) {
		this.scene = scene;
	}
	
	public int getTimestep() {
		if(!this.timestepTextField.getText().isEmpty()) {
			return Integer.parseInt(this.timestepTextField.getText());
		}
		return SimulationEngineConfigDefaults.getDefaultTimestep();
	}
	
	public int getIterationCount() {
		if(!this.iterationCountTextField.getText().isEmpty()) {
			return Integer.parseInt(this.iterationCountTextField.getText());
		}
		return SimulationEngineConfigDefaults.getDefaultStepCount();
	}
	
	public int getBufferSize() {
		if(!this.bufferSizeTextField.getText().isEmpty()) {
			return Integer.parseInt(this.iterationCountTextField.getText());
		}
		return SimulationEngineConfigDefaults.getDefaultBufferSize();
	}
	
	private void loadNewEngine(SimulationProfile profile) {
		this.simulationEngine = new SimulationEngine(profile, getBufferSize(), getTimestep(), getIterationCount());
		
		this.barChartView.updateView(simulationEngine);
		
		this.simulationEngine.addListener((res) -> {
			MainController.this.electedCommitteeLabel.setText("Elected committee : " + res.getElectedCommittee().toString());
		});
	}
}
