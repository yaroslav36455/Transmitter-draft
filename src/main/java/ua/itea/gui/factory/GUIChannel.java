package ua.itea.gui.factory;

import javafx.scene.Node;
import ua.itea.gui.GUIChannelController;

public interface GUIChannel {
	public Node getNode();
	public GUIChannelController getController();
}
