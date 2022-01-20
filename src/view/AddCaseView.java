//Mingyuan Fu (mingyuaf)
package view;

import javafx.scene.Scene;
import javafx.stage.Stage;

public class AddCaseView extends CaseView {

	/**
	 * The constructor will set the updateButton's text to "Add Case"
	 * @param header
	 */
	AddCaseView(String header) {
		super(header);
		this.updateButton.setText("Add Case");
	}
	
	/**
	 * This method will show the window for adding a case
	 */
	@Override
	Stage buildView(){
		Scene addCaseView = new Scene(updateCaseGridPane,CASE_WIDTH,CASE_HEIGHT);
		stage.setScene(addCaseView);
		return stage;
	}

}
