//Mingyuan Fu (mingyuaf)
package model;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

@SuppressWarnings("serial")
public class DataException extends RuntimeException{
	String message;
	
	/**
	 * The constructor takes a message as the warning content.
	 * @param message
	 */
	DataException(String message) {
		this.message = message;
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Data Error");
		alert.setContentText(message);
		alert.showAndWait();
	}
	
	
	
	
}

