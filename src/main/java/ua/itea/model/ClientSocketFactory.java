package ua.itea.model;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class ClientSocketFactory implements SocketFactory {

	@Override
	public Socket create() throws UnknownHostException, IOException {
		return new Socket("127.0.0.1", 44444);
	}

}
