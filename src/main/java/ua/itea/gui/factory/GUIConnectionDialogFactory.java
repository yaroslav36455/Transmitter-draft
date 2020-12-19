package ua.itea.gui.factory;

import java.io.IOException;

import javafx.scene.control.Alert;

public interface GUIConnectionDialogFactory {
	public Alert create() throws IOException;
}
