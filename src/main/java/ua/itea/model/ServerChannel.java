package ua.itea.model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ServerChannel extends Channel {
	private Socket socket;
	
	@Override
	public synchronized void run() {
		try (ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
				ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());) {
			
			loop(ois, oos);
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
}
