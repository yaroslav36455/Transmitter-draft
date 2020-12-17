package ua.itea.gui.factory;

import java.io.IOException;
import java.net.URL;

import javafx.fxml.FXMLLoader;
import ua.itea.gui.ApplicationImpl;
import ua.itea.gui.ConnectionDialogSimple;

public class ConnectionDialogSimpleCachedGUIFactory implements ConnectionDialogGUIFactory {
	private static ConnectionDialogSimple CREATE_CONNECTION_DIALOG;
	
	@Override
	public ConnectionDialogSimple create() throws IOException {
		if (CREATE_CONNECTION_DIALOG == null) {
			CREATE_CONNECTION_DIALOG = new ConnectionDialogSimple();
			
			ClassLoader classLoader = ApplicationImpl.class.getClassLoader();
			URL url = classLoader.getResource("create-connection.fxml");
			FXMLLoader loader = new FXMLLoader(url);
			
			loader.setController(CREATE_CONNECTION_DIALOG);
			CREATE_CONNECTION_DIALOG.getDialogPane().setContent(loader.load());
		}

		return CREATE_CONNECTION_DIALOG;
	}

}
