package ua.itea.gui.factory;

import javafx.scene.control.SplitPane;
import ua.itea.gui.ConnectionController;

public class ConnectionSplitPaneGUI implements ConnectionGUI {
	private SplitPane splitPane;
	private ConnectionController controller;

	public ConnectionSplitPaneGUI(SplitPane splitPane, ConnectionController controller) {
		this.splitPane = splitPane;
		this.controller = controller;
	}

	@Override
	public SplitPane getNode() {
		return splitPane;
	}

	@Override
	public ConnectionController getController() {
		return controller;
	}

}
