package gui.upmc.electisim;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.upmc.electisim.ElectionResult;
import org.upmc.electisim.IElectable;
import org.upmc.electisim.SimulationEngine;
import org.upmc.electisim.SimulationProfile;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

public class BarChartView {
	
	private static final int DEFAULT_NUMBER_OF_SERIES = 0;

	private BarChart<String, Number> chart;
	private CategoryAxis xAxis;
	private NumberAxis yAxis;
	
	private int numberOfSeries = 0;
	
	private Map<IElectable, XYChart.Series<String, Number>> graphSeries = new HashMap<>();

	public BarChartView(AnchorPane pane, String xLabel, String yLabel) {
		this(pane, xLabel, yLabel, DEFAULT_NUMBER_OF_SERIES);
	}
	
	public BarChartView(AnchorPane pane, String xLabel, String yLabel, int numberOfSeries) {
		this.xAxis = new CategoryAxis();
		this.xAxis.setLabel(xLabel);
		this.yAxis = new NumberAxis();
		this.yAxis.setLabel(yLabel);
		
		this.chart = new BarChart<>(xAxis, yAxis);
		
		AnchorPane.setTopAnchor(this.chart, 0.0);
		AnchorPane.setBottomAnchor(this.chart, 27.0);
		AnchorPane.setRightAnchor(this.chart, 0.0);
		AnchorPane.setLeftAnchor(this.chart, 0.0);
		
		pane.getChildren().add(this.chart);
		
		this.chart.setAnimated(false);

		this.numberOfSeries = numberOfSeries;
	}
	
	public void updateView(SimulationEngine engine) {		
		engine.addListener(new SimulationEngine.ResultListener() {
			@Override
			public void resultProduced(ElectionResult electionResult) {
				Platform.runLater(() -> {
					refreshBarGraph(electionResult, engine.getSimulationProfile());
					
					for(Map.Entry<IElectable, XYChart.Series<String, Number>> s : graphSeries.entrySet()) {
						IElectable candidate = s.getKey();
						XYChart.Series<String, Number> serie = graphSeries.get(candidate);
						serie.getData().clear();
						System.out.println(candidate.toString());
						System.out.println(candidate.toString() + " :"  + electionResult.getCandidateScore(candidate));
						serie.getData().add(new XYChart.Data<String, Number>(candidate.toString(), electionResult.getCandidateScore(candidate)));
					}
				});

				//MainController.this.resultGraph;
			}
			
		});
		// this.updateBarGraph(engine.getSimulationProfile());	
	}
	
	// TODO : Perhaps remove the profile because we don't truly need it (we can get the candidate
	// from the election result
	private void refreshBarGraph(ElectionResult electionResult, SimulationProfile profile) {
		List<IElectable> candidateList = null;
		List<IElectable> electableList = electionResult.getElectableList();

		if(numberOfSeries > 0 && numberOfSeries < electableList.size()) {
			List<IElectable> kBests = electionResult.getKBests(numberOfSeries);
			List<IElectable> aux = Arrays.asList(new IElectable[electableList.size()]);
			candidateList = new ArrayList<>();
			System.out.println(kBests);
			for(IElectable c : kBests) {
				aux.set(electableList.indexOf(c), c);
			}
			for(IElectable c : aux) {
				if(c != null) {
					candidateList.add(c);
				}
			}
		}
		else {
			candidateList = profile.getCandidateList();
		}

	    xAxis.setCategories(FXCollections.<String>observableArrayList(
	    		 candidateList.stream().map(IElectable::toString).collect(Collectors.toList())));  
	
	
    	chart.getData().clear();
    	graphSeries = new HashMap<>();

	    for(IElectable c : candidateList) {
	    	XYChart.Series<String, Number> newSerie = new XYChart.Series<>();
	    	newSerie.setName(c.toString());
	    	graphSeries.put(c, newSerie);
	    	chart.getData().add(newSerie);
	    }
	}
}
