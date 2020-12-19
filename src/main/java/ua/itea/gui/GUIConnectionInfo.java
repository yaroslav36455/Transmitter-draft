package ua.itea.gui;

import javafx.scene.Node;

public class GUIConnectionInfo {
	private Node node;
	private GUIConnectionInfoController controller;
	
	public GUIConnectionInfo(Node node, GUIConnectionInfoController controller) {
		this.node = node;
		this.controller = controller;
	}
	
	public Node getNode() {
		return node;
	}
	
	public GUIConnectionInfoController getController() {
		return controller;
	}
}
