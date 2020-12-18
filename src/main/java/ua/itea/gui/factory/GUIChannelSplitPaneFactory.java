package ua.itea.gui.factory;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.SplitPane;
import ua.itea.gui.GUIApplicationImpl;
import ua.itea.gui.GUIConnectionController;

public class GUIChannelSplitPaneFactory implements GUIChannelFactory {

	@Override
	public GUIChannel create() throws IOException {
		GUIConnectionController connectionController = new GUIConnectionController();
		FXMLLoader loader = new FXMLLoader(GUIApplicationImpl.class.getClassLoader().getResource("channel.fxml"));
		loader.setController(connectionController);
		SplitPane splitPane = loader.load();
		
		return new GUIChannelSplitPane(splitPane, connectionController);
	}

}
