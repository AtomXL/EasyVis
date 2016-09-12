package application;

import java.io.File;
import java.io.IOException;
import javafx.application.Application;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.scene.chart.BarChart;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

public class Main extends Application {
	
	@FXML private Button loadButton;
	@FXML private Button lineGraphButton;
	@FXML private Button barGraphButton;
	@FXML private Button scatterGraphButton;
	@FXML private ComboBox<String> barX;
	@FXML private ComboBox<String> barY;
	@FXML private BarChart<String, Number> barChart;
	@FXML private Text statusText;
	@FXML private TableView<ObservableList<StringProperty>> dataTable = new TableView<>();
	
	private static final String mainTitle = "EasyVis - Simple Data Visualisations";
	private static GraphData graphData = new GraphData();
	
	
	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("MainInterface.fxml"));
		
		primaryStage.setTitle(mainTitle);	

		Scene scene = new Scene(root);
		primaryStage.setMinWidth(800);
		primaryStage.setMinHeight(600);
		primaryStage.setScene(scene);
		primaryStage.show();
		
		
	}
	
	@FXML protected void handleLoadButtonPressed (ActionEvent event){
		displayFileChooser();
	}
	
	@FXML protected void handleLineGraphButtonPressed (ActionEvent event){
		
		DynamicLineGraph lineGraph = new DynamicLineGraph();
		
	}
	
	@FXML protected void handleBarGraphButtonPressed (ActionEvent event){
		
		barChart.getData().clear();
		barChart.layout();
		barChart.getData().add(graphData.getXYSeries(barX.getValue(), barY.getValue()));
			
	}
	
	@FXML protected void handleScatterGraphButtonPressed (ActionEvent event){
		
		DynamicScatterGraph scatterGraph = new DynamicScatterGraph();
	
	}

	
	private void displayFileChooser() {
		
		FileChooser chooser = new FileChooser();
		chooser.setTitle("Select a CSV file");
		chooser.setInitialDirectory(new File(System.getProperty("user.home"))); 
		chooser.getExtensionFilters().addAll(new ExtensionFilter("CSV Files", "*.csv"));
		File selectedFile = chooser.showOpenDialog(null);

		if (selectedFile != null && getFileExtension(selectedFile).equals("csv")) {
			
			try {
				statusText.setText(graphData.importCSV(selectedFile));
			} catch (IOException e) {
				statusText.setText("File import failed.");
				e.printStackTrace();
			}
			
			graphData.fillTable(dataTable);
			
			
			barX.setItems(graphData.getHeaders());
			barY.setItems(graphData.getHeaders());
			
		}
		else {
			statusText.setText("File selection cancelled.");
		}
				
	}
	
	private String getFileExtension(File file) {
        String fileName = file.getName();
        if(fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)
        return fileName.substring(fileName.lastIndexOf(".")+1);
        else return "";
    }
	
}
