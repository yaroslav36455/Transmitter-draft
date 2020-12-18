package ua.itea.gui.factory;

import javafx.scene.Node;
import ua.itea.gui.GUIConnectionController;

public interface GUIChannel {
	public Node getNode();
	public GUIConnectionController getController();
}
