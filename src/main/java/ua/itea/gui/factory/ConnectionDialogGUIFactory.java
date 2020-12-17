package ua.itea.gui.factory;

import java.io.IOException;

import javafx.scene.control.Dialog;
import ua.itea.gui.ConnectionInfo;

public interface ConnectionDialogGUIFactory {
	public Dialog<ConnectionInfo> create() throws IOException;
}
