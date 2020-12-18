package ua.itea.gui.factory;

import javafx.scene.control.SplitPane;
import ua.itea.gui.GUIConnectionController;

public class GUIChannelSplitPane implements GUIChannel {
	private SplitPane splitPane;
	private GUIConnectionController controller;

	public GUIChannelSplitPane(SplitPane splitPane, GUIConnectionController controller) {
		this.splitPane = splitPane;
		this.controller = controller;
	}

	@Override
	public SplitPane getNode() {
		return splitPane;
	}

	@Override
	public GUIConnectionController getController() {
		return controller;
	}

}
