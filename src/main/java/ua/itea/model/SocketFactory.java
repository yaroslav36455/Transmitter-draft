package ua.itea.model;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public interface SocketFactory {

	public Socket create() throws UnknownHostException, IOException;

}
