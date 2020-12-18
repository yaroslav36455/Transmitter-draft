package ua.itea.model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Channel implements Runnable, AutoCloseable {
	private LocalFileBase localFileBase;
	private Reader reader;
	private Writer writer;
	private Socket socket;
	private boolean server;

	public Channel() {
		this.reader = new Reader();
		this.writer = new Writer();
		this.writer.setDownloadLimit(new LoadLimit(50));
	}
	
	public void start(boolean server) {
		this.server = server;
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

	@Override
	public synchronized void run() {
		
		System.out.println("begin");

		ObjectInputStream ois = null;
		ObjectOutputStream oos = null;
		
		try {
			System.out.println(server);
			if (server) {
				ois = new ObjectInputStream(socket.getInputStream());
				oos = new ObjectOutputStream(socket.getOutputStream());
			} else {
				oos = new ObjectOutputStream(socket.getOutputStream());
				ois = new ObjectInputStream(socket.getInputStream());
			}
			System.out.println(server);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		try {
			if (!server) {
				Message message = new Message();
				message.setDataRequest(writer.createRequest());
				oos.writeObject(message);
				
				DataFileRequest dfr = message.getDataRequest().iterator().next();
				System.out.println(dfr.getFileId().get());
				System.out.println(dfr.getDataBlockInfo().getOffset());
				System.out.println(dfr.getDataBlockInfo().getSize());
			}
			
//			try {
//				System.out.println("wait");
//				wait();
//			} catch (InterruptedException e1) {
//				e1.printStackTrace();
//			}
			
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
//				message = new Message();
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
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}

	}
}
