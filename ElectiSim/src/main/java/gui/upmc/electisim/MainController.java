package gui.upmc.electisim;

import org.upmc.electisim.SimulationEngine;

import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TextField;

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
	private Menu fileMenu;
	
	@FXML
	private Menu viewMenu;
	
	@FXML
	private Menu aboutMenu;
	
	private SimulationEngine simulationEngine;
	
	
	@FXML
	public void initialize() {
		simulationEngine = null;
		
	}
}
