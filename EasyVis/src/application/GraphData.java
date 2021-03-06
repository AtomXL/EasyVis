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
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.util.Callback;

public class GraphData {
	
	private ArrayList<String> headers;
	private ArrayList<ArrayList<String>> dataRows;
	public GraphData() {
		super();
	}
	
	public String importCSV (File file) throws IOException {		
		headers = new ArrayList<String>();
		dataRows = new ArrayList<ArrayList<String>>();
		
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
		    reader.close();
		    return "File imported: " + file.getName();
    	} else {
    		reader.close();
    		return "File is empty";
    	}
	}
		
	public ObservableList<String> getHeaders() {
		ObservableList<String> oHeaders = FXCollections.observableArrayList(headers);
		
		return oHeaders;
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
	
	private TableColumn<ObservableList<StringProperty>, String> createColumn(final int index, String title) {
		    TableColumn<ObservableList<StringProperty>, String> column = new TableColumn<>();
		    String newTitle;
		    if (title == null || title.trim().length() == 0) {
		      newTitle = "Column " + (index + 1);
		    } else {
		      newTitle = title;
		    }
		    column.setText(newTitle);
		    column
		        .setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList<StringProperty>, String>, ObservableValue<String>>() {
		          @Override
		          public ObservableValue<String> call(
		              CellDataFeatures<ObservableList<StringProperty>, String> cellDataFeatures) {
		            ObservableList<StringProperty> values = cellDataFeatures.getValue();
		            if (index >= values.size()) {
		              return new SimpleStringProperty("");
		            } else {
		              return cellDataFeatures.getValue().get(index);
		            }
		          }
		        });
		    return column;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public XYChart.Series getXYSeries (String xHeader, String yHeader) {
		
		XYChart.Series series = new XYChart.Series();
		int xHeaderPos = 0;
		int yHeaderPos = 1;
		
		for (int column = 0; column < headers.size(); column++) {
			if (headers.get(column).equals(xHeader)) {
				xHeaderPos = column;
			}
			if (headers.get(column).equals(yHeader)) {
				yHeaderPos = column;
			}
		}
			
		for (int row = 0; row < dataRows.size(); row++) {
			
			try{
				series.getData().add(new XYChart.Data(dataRows.get(row).get(xHeaderPos),
					Double.parseDouble(dataRows.get(row).get(yHeaderPos))));
			}
			catch (Exception e){
				series.getData().add(new XYChart.Data(dataRows.get(row).get(xHeaderPos),
						0d));
			}
			
		}
			
		
		
		return series;
	}

	
}
