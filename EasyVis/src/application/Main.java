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
import javafx.scene.control.TableView;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

public class Main extends Application {
	
	@FXML private Button loadButton;
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
	
	@FXML protected void handleLoadButtomPressed (ActionEvent event){
		displayFileChooser();
	}
	
	private void displayFileChooser() {
		
		FileChooser chooser = new FileChooser();
		chooser.setTitle("Select a CSV file");
		chooser.setInitialDirectory(
	            new File(System.getProperty("user.home"))
	        ); 
		chooser.getExtensionFilters().addAll(
				new ExtensionFilter("CSV Files", "*.csv")
				);
		File selectedFile = chooser.showOpenDialog(null);

		if (selectedFile != null && getFileExtension(selectedFile).equals("csv")) {

			statusText.setText("File selected: " + selectedFile.getName());
			
			try {
				graphData.importCSV(selectedFile);
			} catch (IOException e) {
				statusText.setText("File import failed.");
				e.printStackTrace();
			}
			
			graphData.fillTable(dataTable);
			
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
