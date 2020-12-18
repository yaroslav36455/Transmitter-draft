package ua.itea.model;

import java.net.Socket;

public interface ChannelProvider {
	public void establish(Socket socket);
}
