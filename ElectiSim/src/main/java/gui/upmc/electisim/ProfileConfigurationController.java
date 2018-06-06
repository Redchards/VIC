package gui.upmc.electisim;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.UnaryOperator;

import org.reflections.Reflections;
import org.upmc.electisim.Agent;
import org.upmc.electisim.Candidate;
import org.upmc.electisim.IAgentGenerator;
import org.upmc.electisim.IAgentStrategy;
import org.upmc.electisim.ICandidateGenerator;
import org.upmc.electisim.IElectable;
import org.upmc.electisim.IPreferencesGenerator;
import org.upmc.electisim.IVotingRule;
import org.upmc.electisim.PreferenceType;
import org.upmc.electisim.Preferences;
import org.upmc.electisim.RandomPreferencesGenerator;
import org.upmc.electisim.SimpleAgentGenerator;
import org.upmc.electisim.SimpleCandidateGenerator;
import org.upmc.electisim.SimulationProfile;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class ProfileConfigurationController {

	@FXML
	private ChoiceBox<Class<? extends IAgentStrategy>> strategyChoiceBox;
	
	@FXML
	private ChoiceBox<Class<? extends IVotingRule>> votingRuleChoiceBox;
	
	@FXML
	private ChoiceBox<PreferenceType> preferenceTypeChoiceBox;
	
	@FXML
	private TextField addNCandidatesTextField;
	
	@FXML
	private TextField addNAgentsTextField;
	
	@FXML
	private Button addNCandidatesButton;
	
	@FXML
	private Button addNAgentsButton;
	
	@FXML
	private Button generateRandomPreferencesButton;
	
	@FXML
	private ListView<IElectable> candidateListView;
	
	@FXML
	private ListView<Agent> agentListView;
	
	@FXML
	private TextField addCandidateTextField;
	
	@FXML
	private Button addCandidateButton;
	
	@FXML
	private TextField addAgentTextField;
	
	@FXML
	private Button addAgentButton;
	
	@FXML
	private TextField committeeSizeTextField;
	
	@FXML
	private Button confirmButton;
	
	@FXML
	private Button cancelButton;
	
	@FXML
	private Button applyButton;
	
	private SimulationProfile profile;
	
	private IAgentGenerator agentGenerator = new SimpleAgentGenerator();
	private ICandidateGenerator candidateGenerator = new SimpleCandidateGenerator();
	private IPreferencesGenerator prefGenerator = new RandomPreferencesGenerator();
	
	// TODO : Set the modified handler of preferenceTypeChoiceBox so that it update agents' preferences.
	@FXML
	void initialize() {
		loadProfile(profile);
		
		this.addNAgentsTextField.setTextFormatter(FormatterProvider.getNumberFormatter());
		this.addNCandidatesTextField.setTextFormatter(FormatterProvider.getNumberFormatter());
		this.committeeSizeTextField.setTextFormatter(FormatterProvider.getNumberFormatter());

		this.committeeSizeTextField.setOnAction((action) -> {
			if(getPreferenceType() == PreferenceType.HAMMING) {
				Alert alert = new Alert(AlertType.CONFIRMATION);
				alert.setTitle("Committee size modification");
				alert.setHeaderText("Preferences reset");
				alert.setContentText("Changing the size of the committee will reset the agents' preferences for Hamming preferences. Do you want to proceed ?");
	
				Optional<ButtonType> result = alert.showAndWait();
				if (result.get() == ButtonType.OK){
					for(int i = 0; i < getAgentList().size(); i++) {
						Preferences newPref = new Preferences(getPreferenceType(), new ArrayList<>());
						this.agentListView.getItems().set(i, new Agent(this.agentListView.getItems().get(i).getName(), newPref));
					}
				}
				else {
					// TODO : reinit field
				}
			}
		});
		
		this.agentListView.setOnMouseClicked((click) -> {
			if(click.getClickCount() == 2) {
				Agent selectedItem = getSelectedAgent();
				Optional<Agent> newAgent;
				try {
					newAgent = ConfigurationUtils.showAgentConfigurationBox(selectedItem, getPreferenceType(), candidateListView.getItems());
					if(newAgent.isPresent()) {
						for(Agent a : agentListView.getItems()) {
							if(a != selectedItem && a.getName().equals(newAgent.get().getName())) {
								Platform.runLater(() -> DialogBoxHelper.displayWarning("Unable to take the modifications into account", "the agent '" + newAgent.get().getName() + "' already exists"));
								return;
							}
						}
						agentListView.getItems().set(getSelectedAgentIndex(), newAgent.get());
					}
				} catch (AgentConfigurationException e) {
					Platform.runLater(() -> DialogBoxHelper.displayWarning("Unable to take the modifications into account", e.getMessage()));
				}
			}
		});
		
		this.agentListView.addEventHandler(KeyEvent.KEY_PRESSED, ev -> {
	        if (ev.getCode() == KeyCode.DELETE) {
		           agentListView.getItems().remove(getSelectedAgentIndex());
		        }
		    });
		
		this.candidateListView.setOnMouseClicked((click) -> {
			if(click.getClickCount() == 2) {
				IElectable selectedItem = getSelectedCandidate();
				Optional<IElectable> newCandidate;
				try {
					newCandidate = ConfigurationUtils.showCandidateConfigurationBox(selectedItem, candidateListView.getItems());
					if(newCandidate.isPresent()) {
						List<Agent> newAgentList = new ArrayList<>();
						
						for(Agent agent : agentListView.getItems()) {
							List<IElectable> prefList = agent.getPreferences().getPreferenceList();
							int idx = prefList.indexOf(selectedItem);
							if(idx != -1) {
								prefList.set(idx,  newCandidate.get());
							}
							
							newAgentList.add(new Agent(agent.getName(), new Preferences(agent.getPreferences().getType(), prefList)));
						}
						
						agentListView.setItems(FXCollections.observableArrayList(newAgentList));
						candidateListView.getItems().set(getSelectedCandidateIndex(), newCandidate.get());
					}
				} catch (CandidateConfigurationException e) {
					Platform.runLater(() -> DialogBoxHelper.displayWarning("Unable to take the modifications into account", e.getMessage()));
				}
			}
		});
		
		this.candidateListView.addEventHandler(KeyEvent.KEY_PRESSED, ev -> {
	        if(ev.getCode() == KeyCode.DELETE) {
	        		IElectable selectedCandidate = getSelectedCandidate();
	        		List<Agent> newAgentList = new ArrayList<>();
					
					for(Agent agent : agentListView.getItems()) {
						List<IElectable> prefList = agent.getPreferences().getPreferenceList();
						int idx = prefList.indexOf(selectedCandidate);
						if(idx != -1) {
							prefList.remove(idx);
						}
						
						newAgentList.add(new Agent(agent.getName(), new Preferences(agent.getPreferences().getType(), prefList)));
					}
					
					agentListView.setItems(FXCollections.observableArrayList(newAgentList));	        	
					candidateListView.getItems().remove(selectedCandidate);
	        }
		});
		
		Runnable addAgentAction = () -> {
			String newAgentName = addAgentTextField.getText();

			if(newAgentName != null) {
				PreferenceType type = getPreferenceType();
				
				for(Agent a : agentListView.getItems()) {
					if(a.getName().equals(newAgentName)) {
						addAgentTextField.clear();
						Platform.runLater(() -> DialogBoxHelper.displayWarning("Unable to add agent", "The agent '" + newAgentName + "' already exists !"));
						return;
					}
				}
				
				agentListView.getItems().add(new Agent(addAgentTextField.getText(), new Preferences(type, new ArrayList<>())));
				addAgentTextField.clear();
			}
		};
		
		Runnable addCandidateAction = () -> {
			String newCandidateName = addCandidateTextField.getText();

			if(newCandidateName != null) {

				for(IElectable c : candidateListView.getItems()) {
					if(c.getName().equals(newCandidateName)) {
						addCandidateTextField.clear();
						Platform.runLater(() -> DialogBoxHelper.displayWarning("Unable to add candidate", "The candidate '" + newCandidateName + "' already exists !"));
						return;
					}
				}
				
				candidateListView.getItems().add(new Candidate(addCandidateTextField.getText()));
				addCandidateTextField.clear();
			}
		};
		
		this.addAgentTextField.addEventHandler(KeyEvent.KEY_PRESSED, ev -> {
			if(ev.getCode() == KeyCode.ENTER) {
				addAgentAction.run();
			}
		});
		
		this.addAgentButton.setOnAction((evt) -> {
			addAgentAction.run();
		});
		
		this.addCandidateTextField.addEventHandler(KeyEvent.KEY_PRESSED, ev -> {
			if(ev.getCode() == KeyCode.ENTER) {
				addCandidateAction.run();
			}
		});		
		
		this.addCandidateButton.setOnAction((evt) -> {
			addCandidateAction.run();
		});
		
		Runnable addNAgentsAction = () -> {
			String numberToAdd = addNAgentsTextField.getText();
			if(numberToAdd != null) {
				List<Agent> agentList = getAgentList();
				PreferenceType type = getPreferenceType();
				
				for(int i = 0; i < Integer.parseInt(numberToAdd); i++) {
					agentListView.getItems().add(agentGenerator.generate(agentList, type));
				}
			}
			
		};
		
		Runnable addNCandidatesAction = () -> {
			String numberToAdd = addNCandidatesTextField.getText();
			if(numberToAdd != null) {
				List<IElectable> candidateList = getCandidateList();
				PreferenceType type = getPreferenceType();				
				for(int i = 0; i < Integer.parseInt(numberToAdd); i++) {
					candidateListView.getItems().add(candidateGenerator.generate(candidateList));
				}
			}
			
		};
		
		this.addNAgentsTextField.addEventHandler(KeyEvent.KEY_PRESSED, ev -> {
			if(ev.getCode() == KeyCode.ENTER) {
				addNAgentsAction.run();
				addNAgentsTextField.clear();
			}
		});
		
		this.addNAgentsButton.setOnAction((evt) -> {
			addNAgentsAction.run();
			addNAgentsTextField.clear();
		});
		
		this.addNCandidatesTextField.addEventHandler(KeyEvent.KEY_PRESSED, ev -> {
			if(ev.getCode() == KeyCode.ENTER) {
				addNCandidatesAction.run();
				addNCandidatesTextField.clear();
			}
		});
		
		this.addNCandidatesButton.setOnAction((evt) -> {
			addNCandidatesAction.run();
			addNCandidatesTextField.clear();
		});
		
		this.generateRandomPreferencesButton.setOnAction((evt) -> {
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Generation of random preferences");
			alert.setHeaderText("Attempting to generate new preferences");
			alert.setContentText("Generating new preferences will erase the old ones. Do you want to proceed ?");

			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == ButtonType.OK){
				for(int i = 0; i < getAgentList().size(); i++) {
					Preferences newPref = this.prefGenerator.generate(getCandidateList(), getPreferenceType(), getCommitteeSize());
					this.agentListView.getItems().set(i, new Agent(this.agentListView.getItems().get(i).getName(), newPref));
				}
			}
		});
		
		this.cancelButton.setOnAction(action -> {
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Profile modification");
			alert.setHeaderText("Discarding modifications");
			alert.setContentText("The profile has been modified, any unsaved changes will be lost. Do you want to proceed ?");

			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == ButtonType.OK){
				Stage stage = (Stage) this.confirmButton.getScene().getWindow();
				stage.close();
			}
		});
		
		this.confirmButton.setOnAction(action -> {
			try {
				setSimulationProfile(buildSimulationProfile());
				Stage stage = (Stage) this.confirmButton.getScene().getWindow();
				stage.close();
			} catch (SimulationProfileConfigurationException e) {
				Platform.runLater(() -> DialogBoxHelper.displayWarning("Unable to update the configuration", e.getMessage()));

			}
		});
		
		this.applyButton.setOnAction(action -> {
			try {
				setSimulationProfile(buildSimulationProfile());
			} catch (SimulationProfileConfigurationException e) {
				Platform.runLater(() -> DialogBoxHelper.displayWarning("Unable to update the configuration", e.getMessage()));

			}
		});
	}
		

	public void setSimulationProfile(SimulationProfile profile) {
		this.profile = profile;
		loadProfile(profile);
	}
	
	public SimulationProfile getSimulationProfile() {
		return profile;
	}
	
	public SimulationProfile buildSimulationProfile() throws SimulationProfileConfigurationException {
		try {
			return new SimulationProfile(
					getPreferenceType(),
					getVotingRuleInstance(),
					getAgentStrategy(),
					getAgentList(),
					getCandidateList(),
					getCommitteeSize());
		} catch (InstantiationException | IllegalAccessException e) {
			throw new SimulationProfileConfigurationException("Unable to instatiate the required class", e);
		}
	}
	
	public IVotingRule getVotingRuleInstance() throws InstantiationException, IllegalAccessException {
		return this.votingRuleChoiceBox.getSelectionModel().getSelectedItem().newInstance();
	}
	
	public IAgentStrategy getAgentStrategy() throws InstantiationException, IllegalAccessException {
		return this.strategyChoiceBox.getSelectionModel().getSelectedItem().newInstance();
	}
	
	public PreferenceType getPreferenceType() {
		return this.preferenceTypeChoiceBox.getSelectionModel().getSelectedItem();
	}
	
	public List<IElectable> getCandidateList() {
		return this.candidateListView.getItems();
	}
	
	public List<Agent> getAgentList() {
		return this.agentListView.getItems();
	}
	
	public IElectable getSelectedCandidate() {
		return this.candidateListView.getSelectionModel().getSelectedItem();
	}
	
	public int getSelectedCandidateIndex() {
		return this.candidateListView.getSelectionModel().getSelectedIndex();
	}
	
	public Agent getSelectedAgent() {
		return this.agentListView.getSelectionModel().getSelectedItem();
	}
	
	public int getSelectedAgentIndex() {
		return this.agentListView.getSelectionModel().getSelectedIndex();
	}
	
	public Integer getCommitteeSize() {
		if(!this.committeeSizeTextField.getText().isEmpty()) {
			return Integer.parseInt(this.committeeSizeTextField.getText());
		}
		return null;
	}
	
	public void loadProfile(SimulationProfile profile) {
		addNCandidatesTextField.setTextFormatter(FormatterProvider.getNumberFormatter());
		addNAgentsTextField.setTextFormatter(FormatterProvider.getNumberFormatter());
		
		Reflections reflections = new Reflections("org.upmc.electisim");
		//Reflections reflections = new Reflections("com.mycompany");    
		Set<Class<? extends IVotingRule>> reflectedVotingRules = reflections.getSubTypesOf(IVotingRule.class);
		List<Class<? extends IVotingRule>> votingRuleList = new ArrayList<>(reflectedVotingRules);
		this.votingRuleChoiceBox.setTooltip(new Tooltip("Select the voting rule"));	
		
		this.votingRuleChoiceBox.setItems(FXCollections.observableArrayList(votingRuleList));
		
		if(this.profile != null) {
			this.votingRuleChoiceBox.getSelectionModel().select(votingRuleList.indexOf(this.profile.getVotingRule().getClass()));
		}
		else {
			this.votingRuleChoiceBox.getSelectionModel().selectFirst();
		}
		
		Set<Class<? extends IAgentStrategy>> reflectedAgentStrategies = reflections.getSubTypesOf(IAgentStrategy.class);
		
		List<Class<? extends IAgentStrategy>> agentStrategyList = new ArrayList<>(reflectedAgentStrategies);
		this.strategyChoiceBox.setTooltip(new Tooltip("Select the agent strategy"));	
		
		for(Class<? extends IAgentStrategy> strategy : reflectedAgentStrategies) {
			if(Modifier.isAbstract(strategy.getModifiers())) {
				agentStrategyList.remove(strategy);
			}
		}
		
		this.strategyChoiceBox.setItems(FXCollections.observableArrayList(agentStrategyList));
		
		if(this.profile != null) {
			this.strategyChoiceBox.getSelectionModel().select(agentStrategyList.indexOf(this.profile.getVotingStrategy().getClass()));
		}
		else {
			this.strategyChoiceBox.getSelectionModel().selectFirst();
		}
		
		List<PreferenceType> preferenceTypeList = Arrays.asList(PreferenceType.values());
		
		this.preferenceTypeChoiceBox.setItems(FXCollections.observableArrayList(preferenceTypeList));
	
		if(this.profile != null) {
			this.preferenceTypeChoiceBox.getSelectionModel().select(preferenceTypeList.indexOf(this.profile.getPreferenceType()));
		}
		else {
			this.preferenceTypeChoiceBox.getSelectionModel().selectFirst();
		}
		
		List<Agent> agentList;
		List<IElectable> candidateList;
		
		if(this.profile != null) {
			agentList = new ArrayList<>(this.profile.getAgentList());
			candidateList = new ArrayList<>(this.profile.getCandidateList());
		}
		else {
			agentList = new ArrayList<>();
			candidateList = new ArrayList<>();
		}
		
		this.agentListView.setItems(FXCollections.observableArrayList(agentList));
		this.candidateListView.setItems(FXCollections.observableArrayList(candidateList));
		this.agentListView.setEditable(true);
		this.candidateListView.setEditable(true);
		
		if(this.profile != null) {
			this.committeeSizeTextField.setText(Integer.toString(this.profile.getCommitteeSize()));
	
		}
		else {
			this.committeeSizeTextField.setText(Integer.toString(0));
		}
	}
}
