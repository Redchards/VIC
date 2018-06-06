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
import org.upmc.electisim.InvalidStateSteppingException;
import org.upmc.electisim.ElectionResult;
import org.upmc.electisim.SimulationEngine;
import org.upmc.electisim.SimulationProfile;
import org.upmc.electisim.input.SimulationSaveFileReader;
import org.upmc.electisim.output.InvalidExtensionException;
import org.upmc.electisim.output.SimulationSaveFileWriter;
import org.upmc.electisim.utils.EmptyBufferException;
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
import javafx.stage.DirectoryChooser;
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
	
	@FXML
	private Label currentIterationLabel;
	
	@FXML
	private MenuItem barGraphMenuItem;
	
	@FXML
	private MenuItem saveStateMenuItem;
	
	@FXML
	private MenuItem saveBufferMenuItem;
	
	@FXML
	private MenuItem saveProfileMenu;
	
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
			fileChooser.setTitle("Open Configuration File");
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
				this.simulationEngine.setIterationCount(getIterationCount());
				this.simulationEngine.setTimestep(getTimestep());
				
				this.iterationCountTextField.setDisable(true);
				this.bufferSizeTextField.setDisable(true);
				this.timestepTextField.setDisable(true);
				
				Thread runner = new Thread(() -> {
					try {
						this.simulationEngine.run();
					} catch (InterruptedException e) {
						Platform.runLater(() -> DialogBoxHelper.displayError("Error during the simulation", e.getMessage()));
					} finally {
						this.iterationCountTextField.setDisable(false);
						this.bufferSizeTextField.setDisable(false);
						this.timestepTextField.setDisable(false);
					}
				});
				runner.setDaemon(true);
				runner.start();
			}
			else if(this.simulationEngine == null) {
                Platform.runLater(() -> DialogBoxHelper.displayInfo("Unable to launch simulation", "No simulation profile loaded"));
			}
		});
		
		this.runBackwardButton.setOnAction(action -> {
			if(this.simulationEngine != null && !this.simulationEngine.isRunning()) {
				this.simulationEngine.setTimestep(getTimestep());
				
				this.iterationCountTextField.setDisable(true);
				this.bufferSizeTextField.setDisable(true);
				this.timestepTextField.setDisable(true);
				
				Thread runner = new Thread(() -> {
					try {
						this.simulationEngine.runBack();
					} catch (InterruptedException e) {
						Platform.runLater(() -> DialogBoxHelper.displayError("Error during the simulation", e.getMessage()));
					} finally {
						this.iterationCountTextField.setDisable(false);
						this.bufferSizeTextField.setDisable(false);
						this.timestepTextField.setDisable(false);
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
			this.iterationCountTextField.setDisable(false);
			this.bufferSizeTextField.setDisable(false);
			this.timestepTextField.setDisable(false);
			
			if(this.simulationEngine != null) {
				this.simulationEngine.pause();
			}
		});
		
		this.stepButton.setOnAction(action -> {
			this.iterationCountTextField.setDisable(false);
			this.bufferSizeTextField.setDisable(false);
			this.timestepTextField.setDisable(false);
			if(this.simulationEngine != null) {
				this.simulationEngine.setIterationCount(getIterationCount());
				this.simulationEngine.setTimestep(getTimestep());
				if(this.simulationEngine.isRunning()) {
					this.simulationEngine.pause();
				}
				this.simulationEngine.step();
			}
		});
		
		this.stopButton.setOnAction(action -> {
			this.iterationCountTextField.setDisable(false);
			this.bufferSizeTextField.setDisable(false);
			this.timestepTextField.setDisable(false);
			if(this.simulationEngine != null) {
				this.simulationEngine.stop();
			}
		});
		
		this.stepBackButton.setOnAction(action -> {
			this.iterationCountTextField.setDisable(false);
			this.bufferSizeTextField.setDisable(false);
			this.timestepTextField.setDisable(false);
			if(this.simulationEngine != null) {
				if(this.simulationEngine.isRunning()) {
					this.simulationEngine.pause();
				}
				try {
					this.simulationEngine.stepBack();
				} catch (InvalidStateSteppingException e) {
	                Platform.runLater(() -> DialogBoxHelper.displayInfo("Can't step back", "No state remaining in the buffer"));
				}
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
					profile = controller.getSimulationProfile();
						
					loadNewEngine(profile);
				});
				
				stage.initModality(Modality.APPLICATION_MODAL);
				stage.showAndWait();
				
			} catch (IOException e) {
				Platform.runLater(() -> DialogBoxHelper.displayError("Error when loading GUI", e.getMessage()));
			}

		});
		
		Runnable saveState = () -> {
			Window stage = scene.getWindow();
			
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Chose a path to save the state");
            fileChooser.setInitialDirectory(new File(System.getProperty("user.dir")));
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV files (*.csv)", "*.csv"));
			File selectedFile = fileChooser.showSaveDialog(stage);
			
			if(selectedFile != null && simulationEngine != null) {
				try {
					simulationEngine.saveCurrentState(selectedFile);
				} catch (InvalidExtensionException | EmptyBufferException | IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		
		this.saveButton.setOnAction(action -> saveState.run());
		this.saveStateMenuItem.setOnAction(action -> saveState.run());
		this.saveBufferMenuItem.setOnAction(action -> {
			Window stage = scene.getWindow();
			
			DirectoryChooser chooser = new DirectoryChooser();
            chooser.setInitialDirectory(new File(System.getProperty("user.dir")));
            File selectedDirectory = chooser.showDialog(stage);
            
            if(selectedDirectory != null && simulationEngine != null) {
            	try {
					simulationEngine.saveCurrentStateBuffer(selectedDirectory);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
		});
		
		this.saveProfileMenu.setOnAction(action -> {
			Window stage = scene.getWindow();
			
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Chose a path to save the current profile");
            fileChooser.setInitialDirectory(new File(System.getProperty("user.dir")));
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON files (*.json)", "*.json"));
			File selectedFile = fileChooser.showSaveDialog(stage);
			
			if(selectedFile != null && simulationEngine != null) {
				try(SimulationSaveFileWriter writer = new SimulationSaveFileWriter(selectedFile)) {
					writer.writeProfile(simulationEngine.getSimulationProfile());
				} catch (InvalidExtensionException | IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		
		Runnable barChartConfig = () -> {
			BarChartViewConfigBox confBox = new BarChartViewConfigBox(barChartView);
			confBox.showAndWait();
		};
		
		barChartView.setOnMouseClicked((click) -> {
			if(click.getClickCount() == 2) {
				barChartConfig.run();
			}
		});
		
		barGraphMenuItem.setOnAction(action -> {
			barChartConfig.run();
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
			Platform.runLater(() -> MainController.this.electedCommitteeLabel.setText("Elected committee : " + res.getElectedCommittee().toString()));
			Platform.runLater(() -> MainController.this.currentIterationLabel.setText("It : " + simulationEngine.getCurrentIteration()));
		});
		
		this.simulationEngine.addCycleDetectionListener(info -> {
			if(!simulationEngine.getCycleDetector().cycleAlreadyDetected(info)) {
				Platform.runLater(() -> DialogBoxHelper.displayInfo("Cycle detected !", "We detected a cycle in the simulation :\n" + info.toString()));
			}
		});
		
		electedCommitteeLabel.toFront();
		currentIterationLabel.toFront();
		
	}
}
