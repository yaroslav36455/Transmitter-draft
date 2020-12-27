package ua.itea.model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ConnectionClient extends Connection {
	
	public ConnectionClient(Socket socket) throws IOException {
		super(socket);
		oos = new ObjectOutputStream(socket.getOutputStream());
		ois = new ObjectInputStream(socket.getInputStream());
	}
	
	public void writeKey() throws IOException {
		oos.writeLong(getKey());
		oos.flush();
	}

	public void stopServer() throws IOException {
		oos.writeObject(Mark.STOP_SERVER);
	}
}
