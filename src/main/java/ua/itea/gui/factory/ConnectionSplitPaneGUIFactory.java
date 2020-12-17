package ua.itea.gui.factory;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.SplitPane;
import ua.itea.gui.ApplicationImpl;
import ua.itea.gui.ConnectionController;

public class ConnectionSplitPaneGUIFactory implements ConnectionGUIFactory {

	@Override
	public ConnectionGUI create() throws IOException {
		ConnectionController connectionController = new ConnectionController();
		FXMLLoader loader = new FXMLLoader(ApplicationImpl.class.getClassLoader().getResource("connection.fxml"));
		loader.setController(connectionController);
		SplitPane splitPane = loader.load();
		
		return new ConnectionSplitPaneGUI(splitPane, connectionController);
	}

}
