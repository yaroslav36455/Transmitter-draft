package ua.itea.model;

import java.io.IOException;

import ua.itea.model.message.AutoBlockingQueue;
import ua.itea.model.message.Message;
import ua.itea.model.message.StopMessage;

public class Messenger {
	private Thread threadListener;
	private Thread threadSpeaker;
	private AutoBlockingQueue<Message> incomingQueue;
	private AutoBlockingQueue<Message> outgoingQueue;
	private Connection connection;
	private Runnable closeConnectionCallback;
	
	public Connection getConnection() {
		return connection;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	public AutoBlockingQueue<Message> getIncomingQueue() {
		return incomingQueue;
	}

	public void setIncomingQueue(AutoBlockingQueue<Message> incomingQueue) {
		this.incomingQueue = incomingQueue;
	}

	public AutoBlockingQueue<Message> getOutgoingQueue() {
		return outgoingQueue;
	}

	public void setOutgoingQueue(AutoBlockingQueue<Message> outgoingQueue) {
		this.outgoingQueue = outgoingQueue;
	}

	public Runnable getCloseConnectionCallback() {
		return closeConnectionCallback;
	}

	public void setCloseConnectionCallback(Runnable closeConnectionCallback) {
		this.closeConnectionCallback = closeConnectionCallback;
	}

	private void listener() {
		
		try {
			Message message = null;
			
			do {
				message = connection.readMessage();
				incomingQueue.add(message);
			} while(!(message instanceof StopMessage));
			
			StopMessage stopMessage = (StopMessage) message;
			if (stopMessage.isReceiver(this)) {
				stop(stopMessage);
			} else {
				closeConnection();
			}
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	private void speaker() {
		
		try {
			Message message = null;
			
			do {
				message = outgoingQueue.poll();
				connection.writeMessage(message);
			} while(!(message instanceof StopMessage));
			
			StopMessage stopMessage = (StopMessage) message;
			if (stopMessage.isReceiver(this)) {
				closeConnection();
			}
			
		} catch(IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}

	public synchronized void start() {
		try {
			if (threadSpeaker != null && threadSpeaker.isAlive()) {
				threadSpeaker.join();
			}
			
			if (threadListener != null && threadListener.isAlive()) {
				threadListener.join();
			}
			
			threadListener = new Thread(this::listener, "Listener");
			threadSpeaker = new Thread(this::speaker, "Speaker");
			
			threadListener.start();
			threadSpeaker.start();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public synchronized void stop() {
		stop(new StopMessage(this));
	}
	
	private void stop(StopMessage stopMessage) {
		outgoingQueue.add(stopMessage);
	}
	
	public synchronized boolean isConnectionEstablished() {
		return connection != null && !connection.isClosed();
	}
	
	private void closeConnection() {
		try {
			connection.close();
			closeConnectionCallback.run();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void send(Message message) {
		outgoingQueue.add(message);
	}
}
