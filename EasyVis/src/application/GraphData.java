package application;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import com.opencsv.CSVReader;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.util.Callback;

public class GraphData {
	
	private ArrayList<String> headers = new ArrayList<String>();
	private ArrayList<ArrayList<String>> dataRows = new ArrayList<ArrayList<String>>();
	public GraphData() {
		super();
	}
	
	public void importCSV (File file) throws IOException {
		CSVReader reader = new CSVReader(new FileReader(file));
		String[] nextLine =  reader.readNext();
		
    	if (nextLine != null) {
    		
    		for (String col : nextLine) {
    			  headers.add(col);
    		}
    		
		    while ((nextLine = reader.readNext()) != null) {
				ArrayList<String> newRow = new ArrayList<String>();
				
				for (String col : nextLine) {
	    			  newRow.add(col);
	    		}
				
				dataRows.add(newRow);
		    }
    	} else {
    		System.out.println("empty");
    	}
		reader.close();
	}
		
	public ArrayList<String> getHeaders() {
		return headers;
	}

	public void setHeaders(ArrayList<String> headers) {
		this.headers = headers;
	}

	public ArrayList<ArrayList<String>> getDataRows() {
		return dataRows;
	}

	public void setDataRows(ArrayList<ArrayList<String>> dataRows) {
		this.dataRows = dataRows;
	}
	
	public void fillTable(TableView<ObservableList<StringProperty>> table) {
		
		table.getItems().clear();
		table.getColumns().clear();
		table.setPlaceholder(new Label("Loading..."));
		
		for (int column = 0; column < headers.size(); column++) {
			table.getColumns().add(
			createColumn(column, headers.get(column)));
		}
		
		
		for (ArrayList<String> row : dataRows) {
			ObservableList<StringProperty> newRow = FXCollections.observableArrayList();
			for (String entry : row) {
				newRow.add(new SimpleStringProperty(entry));
			}
			
			table.getItems().add(newRow);
		}
		
	}
	
	private TableColumn<ObservableList<StringProperty>, String> createColumn(
		      final int columnIndex, String columnTitle) {
		    TableColumn<ObservableList<StringProperty>, String> column = new TableColumn<>();
		    String title;
		    if (columnTitle == null || columnTitle.trim().length() == 0) {
		      title = "Column " + (columnIndex + 1);
		    } else {
		      title = columnTitle;
		    }
		    column.setText(title);
		    column
		        .setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList<StringProperty>, String>, ObservableValue<String>>() {
		          @Override
		          public ObservableValue<String> call(
		              CellDataFeatures<ObservableList<StringProperty>, String> cellDataFeatures) {
		            ObservableList<StringProperty> values = cellDataFeatures.getValue();
		            if (columnIndex >= values.size()) {
		              return new SimpleStringProperty("");
		            } else {
		              return cellDataFeatures.getValue().get(columnIndex);
		            }
		          }
		        });
		    return column;
	}


	
}
