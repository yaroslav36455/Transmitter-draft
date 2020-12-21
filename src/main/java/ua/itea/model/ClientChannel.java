package ua.itea.model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientChannel extends Channel {
	
	@Override
	public void run() {
		Socket socket = getSocket();
		
		try (ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
				ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());) {
			
			Message message = new Message();
			oos.writeObject(message);
			
			loop(ois, oos);
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
}
