//Mingyuan Fu (mingyuaf)
package view;

import javafx.scene.Scene;
import javafx.stage.Stage;

public class ModifyCaseView extends CaseView {

	/**
	 * The constructor will set the updateButton's text to "Modify Case"
	 * @param header
	 */
	ModifyCaseView(String header) {
		super(header);
		this.updateButton.setText("Modify Case");
	}
	
	/**
	 * This method will show the window for modifying a case
	 */
	@Override
	Stage buildView() {
		Scene modCaseView = new Scene(updateCaseGridPane,CASE_WIDTH,CASE_HEIGHT);
		stage.setScene(modCaseView);
		return stage;
	}

}

	
