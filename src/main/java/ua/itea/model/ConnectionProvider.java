package ua.itea.model;

import java.net.Socket;

public interface ConnectionProvider {
	public void startIncoming(ConnectionServer c);
	public void startOutgoing(ConnectionClient c);
}
