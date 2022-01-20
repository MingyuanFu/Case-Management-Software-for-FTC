//Mingyuan Fu (mingyuaf)
package view;

import javafx.scene.Scene;
import javafx.stage.Stage;

public class DeleteCaseView extends CaseView {

	/**
	 * The constructor will set the updateButton's text to "Delete Case"
	 * @param header
	 */
	DeleteCaseView(String header) {
		super(header);
		this.updateButton.setText("Delete Case");
	}

	/**
	 * This method will show the window for deleting a case
	 */
	@Override
	Stage buildView() {
		Scene delCaseView = new Scene(updateCaseGridPane,CASE_WIDTH,CASE_HEIGHT);
		stage.setScene(delCaseView);
		return stage;
	}

}
