package ua.itea.gui;

import javafx.scene.layout.VBox;

public class GUIChannelVBox implements GUIChannel {
	private VBox vBox;
	private GUIChannelController controller;

	public GUIChannelVBox(VBox vBox, GUIChannelController controller) {
		this.vBox = vBox;
		this.controller = controller;
	}

	@Override
	public VBox getNode() {
		return vBox;
	}

	@Override
	public GUIChannelController getController() {
		return controller;
	}

}
