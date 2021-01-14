package ua.itea.model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import ua.itea.model.message.DataMessage;
import ua.itea.model.message.Message;
import ua.itea.model.message.NewFilesMessage;

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
	
	public void stopReceiver() throws IOException {
		oos.writeObject(Mark.STOP_AS_RECEIVER);
	}
	
	public void stopRequester() throws IOException {
		oos.writeObject(Mark.STOP_AS_REQUESTER);
	}
	
	public void writeName(String name) throws IOException {
		oos.writeUTF(name);
		oos.flush();
	}
	
	public String readName() throws IOException {
		return ois.readUTF();
	}
	
	public void writeDataMessage(DataMessage message) throws IOException {
		oos.writeObject(Mark.DATA_EXCHANGE);
		oos.writeObject(message);
	}
	
	public DataMessage readDataMessage() throws IOException, ClassNotFoundException {
		return (DataMessage) ois.readObject();
	}
	
	public void writeNewFileMessage(NewFilesMessage message) throws IOException {
		oos.writeObject(Mark.NEW_FILES);
		oos.writeObject(message);
	}
	
	public NewFilesMessage readNewFileMessage() throws ClassNotFoundException, IOException {
		return (NewFilesMessage) ois.readObject();
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
	
	public boolean isClosed() {
		return socket.isClosed();
	}
	
	public Message readMessage() throws IOException, ClassNotFoundException {
		return (Message) ois.readObject();
	}

	public void writeMessage(Message message) throws IOException {
		oos.writeObject(message);
	}
}
