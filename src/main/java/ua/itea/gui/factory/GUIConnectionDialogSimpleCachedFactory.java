package ua.itea.gui.factory;

import java.io.IOException;
import java.net.URL;

import javafx.fxml.FXMLLoader;
import ua.itea.gui.GUIApplicationImpl;
import ua.itea.gui.GUIConnectionDialogSimple;

public class GUIConnectionDialogSimpleCachedFactory implements GUIConnectionDialogFactory {
	private static GUIConnectionDialogSimple CREATE_CONNECTION_DIALOG;
	
	@Override
	public GUIConnectionDialogSimple create() throws IOException {
		if (CREATE_CONNECTION_DIALOG == null) {
			CREATE_CONNECTION_DIALOG = new GUIConnectionDialogSimple();
			
			ClassLoader classLoader = GUIApplicationImpl.class.getClassLoader();
			URL url = classLoader.getResource("create-connection.fxml");
			FXMLLoader loader = new FXMLLoader(url);
			
			loader.setController(CREATE_CONNECTION_DIALOG);
			CREATE_CONNECTION_DIALOG.getDialogPane().setContent(loader.load());
		}

		return CREATE_CONNECTION_DIALOG;
	}

}
