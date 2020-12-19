package ua.itea.gui;

import java.io.IOException;

import javafx.fxml.FXMLLoader;

public class GUIConnectionInfoFactory {
	
	public GUIConnectionInfo create() throws IOException {
		GUIConnectionInfoController gcic = new GUIConnectionInfoController();
		FXMLLoader loader = new FXMLLoader();
		
		loader.setLocation(this.getClass().getClassLoader().getResource("connection-info.fxml"));
		loader.setController(gcic);
		
		return new GUIConnectionInfo(loader.load(), gcic);
	}
}
