package ua.itea.gui;

import javafx.fxml.FXML;
import javafx.scene.text.Text;

public class GUIConnectionInfoController {
	@FXML private Text name;
	@FXML private Text address;
	@FXML private Text port;
	
	public Text getName() {
		return name;
	}
	
	public Text getAddress() {
		return address;
	}

	public Text getPort() {
		return port;
	}
	
	public void setInfo() {
		
	}
}
