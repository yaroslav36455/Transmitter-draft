package ua.itea.gui.factory;

import java.io.IOException;

import ua.itea.gui.GUIConnectionInfo;
import ua.itea.gui.GUIIncomingConnectionDialog;

public class GUIIncomingConnectionDialogFactory {
	private GUIConnectionInfo gci;	
	
	public GUIIncomingConnectionDialogFactory(GUIConnectionInfo gci) {
		this.gci = gci;
	}

	public GUIIncomingConnectionDialog create() throws IOException {
		return new GUIIncomingConnectionDialog(gci);
	}
}
