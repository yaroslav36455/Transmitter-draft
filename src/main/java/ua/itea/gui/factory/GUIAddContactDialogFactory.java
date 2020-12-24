package ua.itea.gui.factory;

import java.io.IOException;

import javafx.fxml.FXMLLoader;

public class GUIAddContactDialogFactory {
	public GUIAddContactDialog create() throws IOException {
		FXMLLoader loader = new FXMLLoader();
		GUIAddContactDialog dialog = new GUIAddContactDialog();

		loader.setController(dialog);
		loader.setLocation(this.getClass().getClassLoader().getResource("add-contact.fxml"));
		dialog.getDialogPane().setContent(loader.load());
		
		return dialog;
	}
}
