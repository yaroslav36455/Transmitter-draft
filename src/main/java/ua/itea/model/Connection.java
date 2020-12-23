package ua.itea.model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public abstract class Connection implements AutoCloseable {
	private static final long KEY = (long) (Math.random() * Long.MAX_VALUE);
	
	protected static long getKey() {
		return KEY;
	}
	
	public static boolean isValidKey(long key) {
		return key == getKey();
	}
	
	protected ObjectInputStream ois;
	protected ObjectOutputStream oos;
	protected Socket socket;
	
	public Connection(Socket socket) throws IOException {
		this.socket = socket;
	}
	
	public Socket getSocket() {
		return socket;
	}
	
	public Mark readMark() throws ClassNotFoundException, IOException {
		return (Mark) ois.readObject();
	}
	
	public void start() throws IOException {
		oos.writeObject(Mark.START);
	}
	
	public void stop() throws IOException {
		oos.writeObject(Mark.STOP);
	}
	
	public void writeName(String name) throws IOException {
		oos.writeUTF(name);
		oos.flush();
	}
	
	public String readName() throws IOException {
		return ois.readUTF();
	}
	
	public void write(Message message) throws IOException {
		oos.writeObject(Mark.MESSAGE);
		oos.writeObject(message);
	}
	
	public void read(Message message) throws IOException {
		oos.writeObject(message);
	}
	
	public void accept() throws IOException {
		oos.writeObject(Mark.ACCEPT);
	}
	
	public void reject() throws IOException {
		oos.writeObject(Mark.REJECT);
	}
	
	@Override
	public void close() throws IOException {
		socket.close();
	}
}
