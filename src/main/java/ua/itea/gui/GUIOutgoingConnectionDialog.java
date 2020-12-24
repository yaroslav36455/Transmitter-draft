package ua.itea.gui;

import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.ProgressIndicator;
import ua.itea.model.Mark;

public class GUIOutgoingConnectionDialog extends Dialog<ButtonType> {
	
	public GUIOutgoingConnectionDialog(GUIConnectionInfo gci) {
		getDialogPane().setContent(gci.getNode());
		getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL);
		setTitle("Outgoing connection");
		setHeaderText("Connection...");
		setGraphic(new ProgressIndicator());
	}
	
	public void setAccept() {
		getDialogPane().getButtonTypes().setAll(ButtonType.OK);
		setHeaderText("Answer: " + Mark.ACCEPT);
		
		((ProgressIndicator) getGraphic()).setProgress(1);
	}
	
	public void setReject() {
		getDialogPane().getButtonTypes().setAll(ButtonType.OK);
		setHeaderText("Answer: " + Mark.REJECT);
		
		setGraphic(null);
	}
}
