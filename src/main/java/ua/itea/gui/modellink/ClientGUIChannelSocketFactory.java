package ua.itea.gui.modellink;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import ua.itea.gui.GUIConnectionInfoController;
import ua.itea.model.SocketFactory;

public class ClientGUIChannelSocketFactory implements SocketFactory {
	private GUIConnectionInfoController connectionInfo;
	
	public ClientGUIChannelSocketFactory(GUIConnectionInfoController connectionInfo) {
		this.connectionInfo = connectionInfo;
	}

	@Override
	public Socket create() throws UnknownHostException, IOException {
		String address = connectionInfo.getAddress().getText();
		int port = Integer.valueOf(connectionInfo.getPort().getText());
		
		return new Socket(InetAddress.getByName(address), port);
	}

}
