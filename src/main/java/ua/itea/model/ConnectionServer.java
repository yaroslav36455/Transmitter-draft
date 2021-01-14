package ua.itea.model;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import ua.itea.model.message.Message;

public class ConnectionServer extends Connection {

	public ConnectionServer(Socket socket) throws IOException {
		super(socket);
		ois = new ObjectInputStream(socket.getInputStream());
		oos = new ObjectOutputStream(socket.getOutputStream());
	}
	
	public long readKey() throws IOException { 
		return ois.readLong();
	}
}
