package gui.upmc.electisim;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.reflections.Reflections;
import org.upmc.electisim.IAgentStrategy;
import org.upmc.electisim.IVotingRule;
import org.upmc.electisim.SimulationProfile;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;

public class ProfileConfigurationController {

	@FXML
	private ChoiceBox strategyChoiceBox;
	
	@FXML
	private ChoiceBox votingRuleChoiceBox;
	
	@FXML
	private ChoiceBox preferenceTypeChoiceBox;
	
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
	private ListView candidateListView;
	
	@FXML
	private ListView agentListView;
	
	@FXML
	private TextField addCandidateTextField;
	
	@FXML
	private Button addCandidateButton;
	
	@FXML
	private TextField addAgentTextField;
	
	@FXML
	private Button addAgentButton;
	
	private SimulationProfile profile;
	
	@FXML
	void initialize() {
		
		addNCandidatesTextField.setTextFormatter(FormatterProvider.getNumberFormatter());
		addNAgentsTextField.setTextFormatter(FormatterProvider.getNumberFormatter());
		
		Reflections reflections = new Reflections("org.upmc.electisim");
		//Reflections reflections = new Reflections("com.mycompany");    
		Set<Class<? extends IVotingRule>> reflectedVotingRules = reflections.getSubTypesOf(IVotingRule.class);
		
		List<String> votingRuleList = new ArrayList<>();
		this.votingRuleChoiceBox.setTooltip(new Tooltip("Select the voting rule"));	
		
		for(Class<? extends IVotingRule> clazz : reflectedVotingRules) {
			votingRuleList.add(clazz.getName());
		}
		
		this.votingRuleChoiceBox.setItems(FXCollections.observableArrayList(votingRuleList));
		
		if(this.profile != null) {
			this.votingRuleChoiceBox.getSelectionModel().select(votingRuleList.indexOf(this.profile.getVotingRule().getClass().getName()));
		}
		else {
			this.votingRuleChoiceBox.getSelectionModel().selectFirst();
		}
		
		Set<Class<? extends IAgentStrategy>> reflectedAgentStrategies = reflections.getSubTypesOf(IAgentStrategy.class);
		
		List<String> agentStrategyList = new ArrayList<>();
		this.votingRuleChoiceBox.setTooltip(new Tooltip("Select the voting rule"));	
		
		for(Class<? extends IAgentStrategy> clazz : reflectedAgentStrategies) {
			votingRuleList.add(clazz.getName());
		}
		
		this.strategyChoiceBox.setItems(FXCollections.observableArrayList(votingRuleList));
		
		if(this.profile != null) {
			this.strategyChoiceBox.getSelectionModel().select(votingRuleList.indexOf(this.profile.getVotingStrategy().getClass().getName()));
		}
		else {
			this.strategyChoiceBox.getSelectionModel().selectFirst();
		}
	}
	
	public void setSimulationProfile(SimulationProfile profile) {
		this.profile = profile;
	}
}
