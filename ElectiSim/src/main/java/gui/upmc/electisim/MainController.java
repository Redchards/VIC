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
	private BarChart<String, Number> resultGraph;
	
	@FXML
	private Label electedCommitteeLabel;
	
	@FXML
	private MenuItem loadProfileMenu;
	
	@FXML
	private CategoryAxis graphXAxis;
	
	@FXML
	private NumberAxis graphYAxis;
	
	@FXML
	private Button editProfileButton;
	
	private SimulationEngine simulationEngine;
	
	private Scene scene;
	
	private Map<IElectable, XYChart.Series<String, Number>> graphSeries = new HashMap<>();
	
	
	@FXML
	void initialize() {
		simulationEngine = null;
		
		// this.resultGraph.setTitle("Results");
		
		this.resultGraph.setAnimated(false);
		
		this.committeeSizeTextField.setTextFormatter(FormatterProvider.getNumberFormatter());
		this.iterationCountTextField.setTextFormatter(FormatterProvider.getNumberFormatter());
		this.bufferSizeTextField.setTextFormatter(FormatterProvider.getNumberFormatter());
		this.timestepTextField.setTextFormatter(FormatterProvider.getNumberFormatter());
		
		this.committeeSizeTextField.setOnAction((action) -> {
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Committee size modification");
			alert.setHeaderText("Simulation reset");
			alert.setContentText("Changing the size of the committee will reset the simulation. Do you want to proceed ?");

			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == ButtonType.OK){
			    this.simulationEngine.stop();
			    this.simulationEngine.setCommitteeSize(Integer.parseInt(this.committeeSizeTextField.getText()));
			} else {
			    this.committeeSizeTextField.setText(Integer.toString(this.simulationEngine.getCommitteeSize()));
			}
		});
		
		
		
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
					
					this.simulationEngine = new SimulationEngine(profile, committeeSize);
					this.simulationEngine.setTimestep(1000);

					this.bufferSizeTextField.setText(Integer.toString(this.simulationEngine.getStateBuffer().getCapacity()));
					this.iterationCountTextField.setText(Integer.toString(this.simulationEngine.getIterationCount()));
					this.timestepTextField.setText(Integer.toString(this.simulationEngine.getTimestep()));
					
					
					this.simulationEngine.addListener(new SimulationEngine.ResultListener() {
						@Override
						public void resultProduced(ElectionResult electionResult) {
							Platform.runLater(() -> {
								for(Map.Entry<IElectable, XYChart.Series<String, Number>> s : MainController.this.graphSeries.entrySet()) {
									IElectable candidate = s.getKey();
									XYChart.Series<String, Number> serie = MainController.this.graphSeries.get(candidate);
									serie.getData().clear();
									System.out.println(candidate.toString() + " :"  + electionResult.getCandidateScore(candidate));
									serie.getData().add(new XYChart.Data<String, Number>(candidate.toString(), electionResult.getCandidateScore(candidate)));
								
									MainController.this.electedCommitteeLabel.setText("Elected committee : " + electionResult.getElectedCommittee().toString());
								}
							});

							//MainController.this.resultGraph;
						}
						
					});
					this.updateBarGraph(profile);

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
						simulationEngine.setSimulationProfile(profile);
						updateBarGraph(simulationEngine.getSimulationProfile());
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
		
		System.out.println("Hello");
	}
	
	public void setScene(Scene scene) {
		this.scene = scene;
	}
	
	private void updateBarGraph(SimulationProfile profile) {
		final List<IElectable> candidateList = profile.getCandidateList();
		
		graphXAxis.setLabel("Candidates");
		graphYAxis.setLabel("Scores");
		
	    graphXAxis.setCategories(FXCollections.<String>observableArrayList(
	    		 candidateList.stream().map(c -> c.toString()).collect(Collectors.toList())));  
	
	
    	resultGraph.getData().clear();

	    for(IElectable c : candidateList) {
	    	XYChart.Series<String, Number> newSerie = new XYChart.Series<>();
	    	newSerie.setName(c.toString());
	    	graphSeries.put(c, newSerie);
	    	resultGraph.getData().add(newSerie);
	    }
	}
}
