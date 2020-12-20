package ua.itea.model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public abstract class Channel implements Runnable, AutoCloseable {
	private LocalFileBase localFileBase;
	private Reader reader;
	private Writer writer;
	private Socket socket;

	public Channel() {
		this.reader = new Reader();
		this.writer = new Writer();
		this.writer.setDownloadLimit(new LoadLimit(50));
	}
	
	public void start() {
		new Thread(this).start();
	}
	
	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}

	public LocalFileBase getLocalFileBase() {
		return localFileBase;
	}
	
	public void setLocalFileBase(LocalFileBase localFileBase) {
		this.localFileBase = localFileBase;
		writer.setFileBase(localFileBase.getWriteableBase());
		reader.setFileBase(localFileBase.getReadableBase());
	}

	@Override
	public void close() throws IOException {
		socket.close();
	}
	
	protected final void loop(ObjectInputStream ois, ObjectOutputStream oos)
							  throws IOException, ClassNotFoundException {
		while (true) {
			Message message = (Message) ois.readObject();
			
			DataRequest remoteRequest = message.getDataRequest();
			DataAnswer remoteAnswer = message.getDataAnswer();
			
			DataAnswer thisAnswer = null;
			DataRequest thisRequest = null;
			
			if (remoteRequest != null) {
				thisAnswer = reader.process(remoteRequest);
			}

			if (remoteAnswer != null) {
				writer.process(remoteAnswer);	
			}
			
			thisRequest = writer.createRequest();
			
			// send message
//			message = new Message();
			message.setDataAnswer(thisAnswer);
			message.setDataRequest(thisRequest);
			
			if (remoteRequest == null && thisRequest == null) {
				try {
					System.out.println("wait");
					wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			oos.writeObject(message);
		}
	}
}
