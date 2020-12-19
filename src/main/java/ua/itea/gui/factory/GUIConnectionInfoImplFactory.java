package ua.itea.gui.factory;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import ua.itea.gui.GUIConnectionInfo;
import ua.itea.gui.GUIConnectionInfoController;

public class GUIConnectionInfoImplFactory implements GUIConnectionInfoFactory {
	
	public GUIConnectionInfo create() throws IOException {
		GUIConnectionInfoController gcic = new GUIConnectionInfoController();
		FXMLLoader loader = new FXMLLoader();
		
		loader.setLocation(this.getClass().getClassLoader().getResource("connection-info.fxml"));
		loader.setController(gcic);
		
		return new GUIConnectionInfo(loader.load(), gcic);
	}
}
