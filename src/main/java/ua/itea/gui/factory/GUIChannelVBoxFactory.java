package ua.itea.gui.factory;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.VBox;
import ua.itea.gui.GUIApplicationImpl;
import ua.itea.gui.GUIChannel;
import ua.itea.gui.GUIChannelController;
import ua.itea.gui.GUIChannelVBox;

public class GUIChannelVBoxFactory implements GUIChannelFactory {

	@Override
	public GUIChannel create() throws IOException {
		GUIChannelController connectionController = new GUIChannelController();
		FXMLLoader loader = new FXMLLoader(GUIApplicationImpl.class.getClassLoader().getResource("channel.fxml"));
		loader.setController(connectionController);
		VBox vBox = loader.load();

		return new GUIChannelVBox(vBox, connectionController);
	}

}
