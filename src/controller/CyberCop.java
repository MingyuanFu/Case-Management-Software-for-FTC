//Mingyuan Fu (mingyuaf)
package controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.List;

import bean.Case;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.CCModel;
import view.CCView;
import model.DataException;
import view.AddCaseView;
import view.CaseView;
import view.DeleteCaseView;
import view.ModifyCaseView;

import java.time.LocalDate;

public class CyberCop extends Application{

	public static final String DEFAULT_PATH = "data"; //folder name where data files are stored
	public static final String DEFAULT_HTML = "/CyberCop.html"; //local HTML
	public static final String APP_TITLE = "Cyber Cop"; //displayed on top of app

	CCView ccView = new CCView();
	CCModel ccModel = new CCModel();

	CaseView caseView; //UI for Add/Modify/Delete menu option

	GridPane cyberCopRoot;
	Stage stage;

	static Case currentCase; //points to the case selected in TableView.

	public static void main(String[] args) {
		launch(args);
	}

	/** start the application and show the opening scene */
	@Override
	public void start(Stage primaryStage) throws Exception {
		stage = primaryStage;
		primaryStage.setTitle("Cyber Cop");
		cyberCopRoot = ccView.setupScreen();  
		setupBindings();
		Scene scene = new Scene(cyberCopRoot, ccView.ccWidth, ccView.ccHeight);
		primaryStage.setScene(scene);
		primaryStage.setMaximized(true);
		ccView.webEngine.load(getClass().getResource(DEFAULT_HTML).toExternalForm());
		primaryStage.show();
	}

	/** setupBindings() binds all GUI components to their handlers.
	 * It also binds disableProperty of menu items and text-fields 
	 * with ccView.isFileOpen so that they are enabled as needed
	 */
	void setupBindings() {
		/*
		 * binding GUI items to opening status,
		 * so that the program can disable the items real-time, when no file is imported 
		 */
		ccView.closeFileMenuItem.disableProperty().bind(Bindings.not(ccView.isFileOpen)); 
		ccView.openFileMenuItem.disableProperty().bind(ccView.isFileOpen); 
		ccView.addCaseMenuItem.disableProperty().bind(Bindings.not(ccView.isFileOpen));
		ccView.modifyCaseMenuItem.disableProperty().bind(Bindings.not(ccView.isFileOpen));
		ccView.deleteCaseMenuItem.disableProperty().bind(Bindings.not(ccView.isFileOpen));
		ccView.yearComboBox.disableProperty().bind(Bindings.not(ccView.isFileOpen));
		ccView.titleTextField.disableProperty().bind(Bindings.not(ccView.isFileOpen));
		ccView.caseTypeTextField.disableProperty().bind(Bindings.not(ccView.isFileOpen));
		ccView.caseNumberTextField.disableProperty().bind(Bindings.not(ccView.isFileOpen));
		ccView.searchButton.disableProperty().bind(Bindings.not(ccView.isFileOpen));
		ccView.clearButton.disableProperty().bind(Bindings.not(ccView.isFileOpen));
		ccView.saveFileMenuItem.disableProperty().bind(Bindings.not(ccView.isFileOpen));
		ccView.caseCountChartMenuItem.disableProperty().bind(Bindings.not(ccView.isFileOpen));
		// setting up EventHandlers to GUI items
		ccView.exitMenuItem.setOnAction(new ExitMenuItemHandler());
		ccView.openFileMenuItem.setOnAction(new OpenMenuItemHandler());
		ccView.closeFileMenuItem.setOnAction(new CloseMenuItemHandler());
		ccView.clearButton.setOnAction(new ClearButtonHandler());
		ccView.searchButton.setOnAction(new SearchButtonHandler());
		ccView.addCaseMenuItem.setOnAction(new CaseMenuItemHandler());
		ccView.modifyCaseMenuItem.setOnAction(new CaseMenuItemHandler());
		ccView.deleteCaseMenuItem.setOnAction(new CaseMenuItemHandler());
		ccView.saveFileMenuItem.setOnAction(new SaveMenuItemHandler());
		ccView.caseCountChartMenuItem.setOnAction(new CaseCountChartMenuItemHandler());
		
		// setting up a change listener so that the text fields can show the case information real-time
		ccView.caseTableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Case>() {

			@SuppressWarnings("rawtypes")
			@Override
			public void changed(ObservableValue observable, Case oldValue, Case newValue) {
				if(newValue!=null) {
					currentCase = newValue;
					ccView.titleTextField.setText(currentCase.getCaseTitle());
					ccView.caseNumberTextField.setText(currentCase.getCaseNumber());
					ccView.caseNotesTextArea.setText(currentCase.getCaseNotes());
					ccView.caseTypeTextField.setText(currentCase.getCaseType());
					
					String currentCaseYear = currentCase.getCaseDate().split("-")[0];
					ccView.yearComboBox.getSelectionModel().select(currentCaseYear);
					
					if (currentCase.getCaseLink() == null || currentCase.getCaseLink().isBlank()) {  //if no link in data
						//URL url = getClass().getClassLoader().getResource(DEFAULT_HTML);  //default html
						URL url = getClass().getResource(DEFAULT_HTML); 
						if (url != null) ccView.webEngine.load(url.toExternalForm());
					} else if (currentCase.getCaseLink().toLowerCase().startsWith("http")){  //if external link
						ccView.webEngine.load(currentCase.getCaseLink());
					} else {
						URL url = getClass().getClassLoader().getResource(currentCase.getCaseLink().trim());  //local link
						if (url != null) ccView.webEngine.load(url.toExternalForm());
					}
					
				}
				
			}
			
		});
		
	}
	
	/** This EventHandler will open and read the opened file, then set up GUI items*/
	private class OpenMenuItemHandler implements EventHandler<ActionEvent>{
		@Override
		public void handle(ActionEvent event) {
			FileChooser fc = new FileChooser(); // open the window to choose a file
			fc.setInitialDirectory(new File(DEFAULT_PATH));
			File userfile = fc.showOpenDialog(stage); //it will return a file the user chooses
			try {
				if(userfile!=null) {
					String filename = userfile.getCanonicalPath();
					ccModel.readCases(filename);
					ccModel.buildYearMapAndList();
					// set up GUI items after opening the file
					ccView.caseTableView.setItems(ccModel.caseList);
					ccView.caseTableView.getSelectionModel().select(0);
					ccView.yearComboBox.setItems(ccModel.yearList);
					ccView.isFileOpen.set(true);
					ccView.messageLabel.setText(ccModel.caseList.size()+" cases");
					}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/** This EventHandler will close the opened file, and clear GUI items and caseList*/
	private class CloseMenuItemHandler implements EventHandler<ActionEvent>{

		@Override
		public void handle(ActionEvent event) {
			ccView.webEngine.load(getClass().getResource(DEFAULT_HTML).toExternalForm());
			ccModel.caseList.clear(); // after closing the file, the caseList should be reset to null
			ccModel.caseMap.clear();
			ccModel.yearList.clear();
			ccModel.yearMap.clear();
			ccView.caseTableView.setItems(null);
			ccView.isFileOpen.set(false);
			// clear GUI items case after closing the file
			ccView.yearComboBox.getSelectionModel().clearSelection();
			ccView.titleTextField.setText("");
			ccView.caseTypeTextField.setText("");
			ccView.caseNumberTextField.setText("");
			ccView.caseNotesTextArea.setText("");
			ccView.messageLabel.setText("");
		}
		
	}
	
	/** This EventHandler will end the program*/
	private class ExitMenuItemHandler implements EventHandler<ActionEvent>{
		@Override
		public void handle(ActionEvent event) {
			Platform.exit();
		}
	}
	
	/** This EventHandler will search for cases based on user input and show found cases*/
	private class SearchButtonHandler implements EventHandler<ActionEvent>{
		@Override
		public void handle(ActionEvent event) {
			String thisTitle = ccView.titleTextField.getText();
			String thisType = ccView.caseTypeTextField.getText();
			String thisNumber = ccView.caseNumberTextField.getText();
			String thisYear = ccView.yearComboBox.getSelectionModel().getSelectedItem();
			List<Case> foundCases = ccModel.searchCases(thisTitle, thisType, thisYear, thisNumber);
			ObservableList<Case> foundObList = FXCollections.observableArrayList(foundCases);
			ccView.caseTableView.setItems(foundObList);
			// the case table will select the first case by default
			ccView.caseTableView.getSelectionModel().select(0);
			ccView.messageLabel.setText(foundObList.size()+" cases");
		}
	}
	
	/** This EventHandler will clear all GUI items*/
	private class ClearButtonHandler implements EventHandler<ActionEvent>{
		@Override
		public void handle(ActionEvent event) {
			ccView.titleTextField.setText("");
			ccView.caseTypeTextField.setText("");
			ccView.caseNumberTextField.setText("");
			ccView.yearComboBox.getSelectionModel().clearSelection();
			ccView.messageLabel.setText("");
		}
	}
	
	/** 
	 * This EventHandler will invoke add/modify/delete handler and set the GUI items accordingly.
	 * It is invoked when user makes a choice in the case menu.
	 * */
	private class CaseMenuItemHandler implements EventHandler<ActionEvent>{
		@Override
		public void handle(ActionEvent event) {
			MenuItem choice = (MenuItem) event.getSource();
			if(choice.getText().equals("Add case")) {
				caseView = new AddCaseView("Add Case");
				caseView.updateButton.setOnAction(new AddButtonHandler());
				
			} else if (choice.getText().equals("Modify case")) {
				caseView = new ModifyCaseView("Modify Case");
				if(currentCase != null) {
					caseView.titleTextField.setText(currentCase.getCaseTitle());
					caseView.caseTypeTextField.setText(currentCase.getCaseType());
					caseView.caseNumberTextField.setText(currentCase.getCaseNumber());
					caseView.categoryTextField.setText(currentCase.getCaseCategory());
					caseView.caseLinkTextField.setText(currentCase.getCaseLink());
					caseView.caseNotesTextArea.setText(currentCase.getCaseNotes());
					String[] ymd = currentCase.getCaseDate().split("-");
					caseView.caseDatePicker.setValue(LocalDate.of(Integer.parseInt(ymd[0]),Integer.parseInt(ymd[1]),Integer.parseInt(ymd[2])));
				}
				caseView.updateButton.setOnAction(new ModifyButtonHandler());
			} else {
				if(currentCase != null) {
					caseView = new DeleteCaseView("Delete Case");
					caseView.titleTextField.setText(currentCase.getCaseTitle());
					caseView.caseTypeTextField.setText(currentCase.getCaseType());
					caseView.caseNumberTextField.setText(currentCase.getCaseNumber());
					caseView.categoryTextField.setText(currentCase.getCaseCategory());
					caseView.caseLinkTextField.setText(currentCase.getCaseLink());
					caseView.caseNotesTextArea.setText(currentCase.getCaseNotes());
					String[] ymd = currentCase.getCaseDate().split("-");
					caseView.caseDatePicker.setValue(LocalDate.of(Integer.parseInt(ymd[0]),Integer.parseInt(ymd[1]),Integer.parseInt(ymd[2])));
				}
				
				caseView.updateButton.setOnAction(new DeleteButtonHandler());
			}
			
			// show the window after clicking add/modify/delete
			Stage thisStage = caseView.buildView();
			//thisStage.setAlwaysOnTop(true);
			thisStage.show();
			
			// clear and close button are the same for add/modify/delete
			caseView.closeButton.setOnAction((closeEvent)->{thisStage.close();});
			caseView.clearButton.setOnAction((clearEvent)->{
				caseView.titleTextField.setText("");
				caseView.caseTypeTextField.setText("");
				caseView.caseNumberTextField.setText("");
				caseView.categoryTextField.setText("");
				caseView.caseLinkTextField.setText("");
				caseView.caseNotesTextArea.setText("");
				caseView.caseDatePicker.setValue(LocalDate.now());
			});
		}
	}
	
	/** This EventHandler will add a case to caseList. After that, the program selects the first case by default. Also, it will update the yearList*/
	private class AddButtonHandler implements EventHandler<ActionEvent>{

		@Override
		public void handle(ActionEvent event) {
			String addTitle = caseView.titleTextField.getText();
			String addType = caseView.caseTypeTextField.getText();
			String addNumber = caseView.caseNumberTextField.getText();
			String addCategory = caseView.categoryTextField.getText();
			String addLink = caseView.caseLinkTextField.getText();
			String addNotes = caseView.caseNotesTextArea.getText();
			String addDate= caseView.caseDatePicker.getValue().toString();
			// checking required fields
			if (addTitle.length() == 0|| addType.length() == 0 || addDate.length() == 0 || addNumber.length() == 0) {
				try {
					throw new DataException("Case must have date, title, type, and number.\n");
				} catch (DataException e) {
					System.out.println("ERROR: When adding this case, " + e.message);
					return;
				}
			} else {
				for (Case theCase : ccModel.caseList) {
					if (theCase.getCaseNumber().equals(addNumber)) {
						// checking items with same case number 
						try {
							throw new DataException("Duplicate case number\n");
						} catch (DataException e) {
							System.out.println("ERROR: When adding this case, " + e.message + "Found");
							return;
						}
					}
				}
			}
			Case newAdded = new Case(addDate,addTitle,addType,addNumber,addLink,addCategory,addNotes);
			ccModel.caseList.add(newAdded);
			ccModel.caseMap.put(addNumber,newAdded);
			// update GUI items
			ccView.caseTableView.setItems(ccModel.caseList);
			ccView.caseTableView.getSelectionModel().select(0);
			ccView.messageLabel.setText(ccModel.caseList.size()+" cases");
			if (!ccModel.yearList.contains(addDate.split("-")[0])) {
				ccModel.yearList.add(addDate.split("-")[0]);
				Collections.sort(ccModel.yearList);
			}
			
		}
		
	}
	
	/** This EventHandler will overwrite the selected case with new information then select the first case by default. Also, it will update the yearList*/ 
	private class ModifyButtonHandler implements EventHandler<ActionEvent>{

		@Override
		public void handle(ActionEvent event) {
			String addTitle = caseView.titleTextField.getText();
			String addType = caseView.caseTypeTextField.getText();
			String addNumber = caseView.caseNumberTextField.getText();
			String addCategory = caseView.categoryTextField.getText();
			String addLink = caseView.caseLinkTextField.getText();
			String addNotes = caseView.caseNotesTextArea.getText();
			String addDate= caseView.caseDatePicker.getValue().toString();
			
			if (addTitle.length() == 0|| addType.length() == 0 || addDate.length() == 0 || addNumber.length() == 0) {
				try {
					// checking required fields
					throw new DataException ("Case must have date, title, type, and number.\n");
				} catch (DataException e) {
					System.out.println("ERROR: When modifying this case, " + e.message + "\n");
					return;
				}
			} else {
				// checking items with same case number 
				Case selected = ccView.caseTableView.getSelectionModel().getSelectedItem();
				Case sameCaseNumer = ccModel.caseMap.get(addNumber);
				if (sameCaseNumer != null && selected != sameCaseNumer && sameCaseNumer.getCaseNumber().equals(addNumber)) {
					try {
						throw new DataException("Duplicate case number\n");
					} catch (DataException e) {
						System.out.println("ERROR: When modifying this case, " + e.message + "\n");
						return;
					}
				}
			}
			
			int index = ccView.caseTableView.getSelectionModel().getSelectedIndex();
			ccModel.caseList.remove(index);
			Case updated = new Case(addDate,addTitle,addType,addNumber,addLink,addCategory,addNotes);
			ccModel.caseList.add(index,updated);
			ccModel.caseMap.put(addNumber,updated);
			// update GUI items
			ccView.caseTableView.setItems(ccModel.caseList);
			ccView.caseTableView.getSelectionModel().select(index);
			ccView.messageLabel.setText(ccModel.caseList.size()+" cases");
			if (!ccModel.yearList.contains(addDate.split("-")[0])) {
				ccModel.yearList.add(addDate.split("-")[0]);
				Collections.sort(ccModel.yearList);
			}
		}
	}
	
	/** This EventHandler will delete the selected case then select the first case by default. Also, it will update the yearList*/
	private class DeleteButtonHandler implements EventHandler<ActionEvent>{

		@Override
		public void handle(ActionEvent event) {
			int index = ccView.caseTableView.getSelectionModel().getSelectedIndex();
			if(ccModel.caseList.size()>=1) {
				ccModel.caseList.remove(index);
				ccModel.caseMap.remove(ccView.caseTableView.getSelectionModel().getSelectedItem().getCaseNumber());
				ccView.caseTableView.setItems(ccModel.caseList);
				ccView.caseTableView.getSelectionModel().select(index-1);
			}
			// update GUI items
			if(ccModel.caseList.size()==0) {
				ccView.titleTextField.setText("");
				ccView.caseTypeTextField.setText("");
				ccView.caseNumberTextField.setText("");
				ccView.yearComboBox.getSelectionModel().clearSelection();
			}
			ccView.messageLabel.setText(ccModel.caseList.size()+" cases");
			ccView.yearComboBox.getItems().clear();
			ccModel.buildYearMapAndList();
			ccView.yearComboBox.setItems(ccModel.yearList);	
		}
	}
	/**
	 * This EventHandler will delete the selected case then select the first case by default. Also, it will update the yearList
	 */
	private class SaveMenuItemHandler implements EventHandler<ActionEvent> {

		@Override
		public void handle(ActionEvent event) {
			FileChooser fc = new FileChooser();
			fc.setInitialDirectory(new File(DEFAULT_PATH));
			File file = fc.showSaveDialog(stage);
	        if (file == null)
	            return;
	        if(file.exists()){
	            file.delete();
	        }
	        String fileName = file.getAbsolutePath();
	        String separator = "/|\\\\";
	        String[] elements = fileName.split(separator);
	        String fileNameInLabel = elements[elements.length-1];
	        boolean saveFlag = ccModel.writeCases(fileName);
	        if (saveFlag) {
	        	ccView.messageLabel.setText(fileNameInLabel+" saved.");
	        } else {
	        	ccView.messageLabel.setText(fileNameInLabel+" failed.");
	        }
	       
		}
	}
	
	private class CaseCountChartMenuItemHandler implements EventHandler<ActionEvent> {

		@Override
		public void handle(ActionEvent event) {
			ccModel.buildYearMapAndList();
			ccView.showChartView(ccModel.yearMap);
		}
		
	}
}

