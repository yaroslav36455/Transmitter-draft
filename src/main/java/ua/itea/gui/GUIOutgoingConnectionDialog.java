package ua.itea.gui;

import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.ProgressIndicator;

public class GUIOutgoingConnectionDialog extends Dialog<ButtonType> {
	
	public GUIOutgoingConnectionDialog(GUIConnectionInfo gci) {
		getDialogPane().setContent(gci.getNode());
		getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL);
		setTitle("Outgoing connection");
		setHeaderText("Connection...");
		setGraphic(new ProgressIndicator());

		setListeners();
	}

	private void setListeners() {
		
	}
}
