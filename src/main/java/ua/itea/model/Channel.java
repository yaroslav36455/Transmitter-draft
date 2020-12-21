package ua.itea.model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public abstract class Channel implements Runnable {
	private Socket socket;
	private Downloader downloader;
	private Uploader uploader;

	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}
	
	public Downloader getDownloader() {
		return downloader;
	}

	public void setDownloader(Downloader downloader) {
		this.downloader = downloader;
	}

	public Uploader getUploader() {
		return uploader;
	}

	public void setUploader(Uploader uploader) {
		this.uploader = uploader;
	}

	public void start() {
		new Thread(this).start();
	}

	protected final void loop(ObjectInputStream ois, ObjectOutputStream oos) 
									throws ClassNotFoundException, IOException {
		boolean run = true;

		while (run) {
			Mark mark = (Mark) ois.readObject();

			switch (mark) {
			case MESSAGE:
				Message incomingMessage = (Message) ois.readObject();
				Message outgoingMessage = createMessage(incomingMessage);
				
				if (outgoingMessage.isEmpty()) {
					run = false;
					oos.writeObject(Mark.STOP);
				} else {
					oos.writeObject(Mark.MESSAGE);
					oos.writeObject(outgoingMessage);
				}
				break;

			default:
				run = false;
				break;
			}
		}
	}
	
	public Message createMessage(Message incomingMessage) {
		Request incomingRequest = incomingMessage.getRequest();
		Answer incomingAnswer = incomingMessage.getAnswer();
		Message outgoingMessag = new Message();
		
		DataRequest incomingDataRequest = incomingRequest.getData();
		DataAnswer incomingDataAnswer = incomingAnswer.getData();
		
		DataAnswer outgoingDataAnswer = uploader.load(incomingDataRequest);
		DataRequest outgoingDataRequest = downloader.load(incomingDataAnswer);
		
		if (outgoingDataAnswer != null) {
			Answer answer = new Answer();
			answer.setData(outgoingDataAnswer);
			outgoingMessag.setAnswer(answer);
		}
		
		if (outgoingDataRequest != null) {
			Request request = new Request();
			request.setData(outgoingDataRequest);
			outgoingMessag.setRequest(request);
		}
		
		return outgoingMessag;
	}
}
