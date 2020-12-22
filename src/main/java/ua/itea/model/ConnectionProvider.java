package ua.itea.model;

import java.net.Socket;

public interface ConnectionProvider {
	public void startIncoming(Socket socket);
	public void startOutgoing(Socket socket);
}
