package ua.itea.gui.factory;

import java.io.IOException;

import ua.itea.gui.GUIConnectionInfo;
import ua.itea.gui.GUIIncomingConnectionDialog;

public class GUIIncomingConnectionDialogFactory {
	private GUIConnectionInfoFactory gcif;	
	
	public GUIIncomingConnectionDialogFactory(GUIConnectionInfoFactory gcif) {
		this.gcif = gcif;
	}

	public GUIIncomingConnectionDialog create() throws IOException {
		GUIConnectionInfo gci = gcif.create();
		gci.getController().getName().setText("<Unknown>");
		gci.getController().getAddress().setText("192.168.0.1");
		gci.getController().getPort().setText("62345");
		
		return new GUIIncomingConnectionDialog(gci);
	}
}
