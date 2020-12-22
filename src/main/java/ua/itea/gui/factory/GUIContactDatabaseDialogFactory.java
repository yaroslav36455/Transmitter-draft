package ua.itea.gui.factory;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import ua.itea.gui.GUIContactDatabaseDialog;

public class GUIContactDatabaseDialogFactory {
	public GUIContactDatabaseDialog create() throws IOException {
		FXMLLoader loader = new FXMLLoader();
		GUIContactDatabaseDialog dialog = new GUIContactDatabaseDialog();

		loader.setController(dialog);
		loader.setLocation(this.getClass().getClassLoader().getResource("contact-database.fxml"));
		dialog.getDialogPane().setContent(loader.load());
		
		return dialog;
	}
}
