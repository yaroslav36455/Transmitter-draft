package ua.itea.gui.factory;

import java.io.IOException;

import javafx.scene.control.Dialog;
import ua.itea.gui.GUIConnectionInfo;

public interface GUIConnectionDialogFactory {
	public Dialog<GUIConnectionInfo> create() throws IOException;
}
