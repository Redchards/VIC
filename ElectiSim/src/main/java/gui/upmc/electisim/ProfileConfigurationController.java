package gui.upmc.electisim;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.reflections.Reflections;
import org.upmc.electisim.Agent;
import org.upmc.electisim.Candidate;
import org.upmc.electisim.IAgentStrategy;
import org.upmc.electisim.IElectable;
import org.upmc.electisim.IVotingRule;
import org.upmc.electisim.PreferenceType;
import org.upmc.electisim.Preferences;
import org.upmc.electisim.SimulationProfile;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
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
	
	private SimulationProfile profile;
	
	// TODO : Set the modified handler of preferenceTypeChoiceBox so that it update agents' preferences.
	@FXML
	void initialize() {
		loadProfile(profile);
		
		this.addNAgentsTextField.setTextFormatter(FormatterProvider.getNumberFormatter());
		this.addNCandidatesTextField.setTextFormatter(FormatterProvider.getNumberFormatter());
		
		this.agentListView.setOnMouseClicked((click) -> {
			if(click.getClickCount() == 2) {
				Agent selectedItem = agentListView.getSelectionModel().getSelectedItem();
				Optional<Agent> newAgent;
				try {
					newAgent = ConfigurationUtils.showAgentConfigurationBox(selectedItem, preferenceTypeChoiceBox.getSelectionModel().getSelectedItem(), candidateListView.getItems());
					if(newAgent.isPresent()) {
						for(Agent a : agentListView.getItems()) {
							if(a != selectedItem && a.getName().equals(newAgent.get().getName())) {
								Platform.runLater(() -> DialogBoxHelper.displayWarning("Unable to take the modifications into account", "the agent '" + newAgent.get().getName() + "' already exists"));
								return;
							}
						}
						agentListView.getItems().set(agentListView.getSelectionModel().getSelectedIndex(), newAgent.get());
					}
				} catch (AgentConfigurationException e) {
					Platform.runLater(() -> DialogBoxHelper.displayWarning("Unable to take the modifications into account", e.getMessage()));
				}
			}
		});
		
		this.agentListView.addEventHandler(KeyEvent.KEY_PRESSED, ev -> {
	        if (ev.getCode() == KeyCode.DELETE) {
		           agentListView.getItems().remove(agentListView.getSelectionModel().getSelectedIndex());
		        }
		    });
		
		this.candidateListView.setOnMouseClicked((click) -> {
			if(click.getClickCount() == 2) {
				IElectable selectedItem = candidateListView.getSelectionModel().getSelectedItem();
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
						candidateListView.getItems().set(candidateListView.getSelectionModel().getSelectedIndex(), newCandidate.get());
					}
				} catch (CandidateConfigurationException e) {
					Platform.runLater(() -> DialogBoxHelper.displayWarning("Unable to take the modifications into account", e.getMessage()));
				}
			}
		});
		
		this.candidateListView.addEventHandler(KeyEvent.KEY_PRESSED, ev -> {
	        if(ev.getCode() == KeyCode.DELETE) {
	        		IElectable selectedCandidate = candidateListView.getSelectionModel().getSelectedItem();
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
				PreferenceType type = this.preferenceTypeChoiceBox.getSelectionModel().getSelectedItem();
				
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
	}
		

	public void setSimulationProfile(SimulationProfile profile) {
		this.profile = profile;
		loadProfile(profile);
	}
	
	public SimulationProfile buildSimulationProfile() throws SimulationProfileConfigurationException {
		try {
			return new SimulationProfile(
					this.preferenceTypeChoiceBox.getSelectionModel().getSelectedItem(),
					this.votingRuleChoiceBox.getSelectionModel().getSelectedItem().newInstance(),
					this.strategyChoiceBox.getSelectionModel().getSelectedItem().newInstance(),
					this.agentListView.getItems(),
					this.candidateListView.getItems());
		} catch (InstantiationException | IllegalAccessException e) {
			throw new SimulationProfileConfigurationException("Unable to instatiate the required class", e);
		}
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
	}
}
