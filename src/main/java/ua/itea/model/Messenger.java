package ua.itea.model;

import java.io.IOException;

import ua.itea.model.message.AutoBlockingQueue;
import ua.itea.model.message.LoaderMessage;
import ua.itea.model.message.Message;

public class Messenger {
	private Thread threadListener;
	private Thread threadSpeaker;
	private boolean runListener;
	private boolean runSpeaker;
	
	private Runnable beginMessaging;
	private Runnable endMessaging;
	
	private AutoBlockingQueue<Message> outgoing;
	private Connection connection;
	private Channel channel;
	
	public Messenger() {
		outgoing = new AutoBlockingQueue<>();
		beginMessaging = ()->{};
		endMessaging = ()->{};
	}
	
	public Runnable getBeginMessaging() {
		return beginMessaging;
	}

	public void setBeginMessaging(Runnable beginMessaging) {
		this.beginMessaging = beginMessaging;
	}

	public Runnable getEndMessaging() {
		return endMessaging;
	}

	public void setEndMessaging(Runnable endMessaging) {
		this.endMessaging = endMessaging;
	}

	public Channel getChannel() {
		return channel;
	}

	public void setChannel(Channel channel) {
		this.channel = channel;
	}

	public Connection getConnection() {
		return connection;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	public AutoBlockingQueue<Message> getOutgoing() {
		return outgoing;
	}

	public void setOutgoing(AutoBlockingQueue<Message> outgoingQueue) {
		this.outgoing = outgoingQueue;
	}
	
	private void listener() {
		
		try {
			runListener = true;
			while(runListener) {
				Message message = connection.readMessage();
				message.execute(this);
			}
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	private void speaker() {
		
		try {
			runSpeaker = true;
			while(runSpeaker) {
				Message message = outgoing.poll();
				connection.writeMessage(message);
			}
		} catch(IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}

	public synchronized void start() {
		threadListener = new Thread(this::listener, "Listener");
		threadSpeaker = new Thread(this::speaker, "Speaker");
		
		threadListener.start();
		threadSpeaker.start();
		
		channel.start(this);
		
		beginMessaging.run();
	}
	
	public synchronized void stop() {
		send(new StopMessage(this));
		stopSpeaker();
	}
	
	private void stopListener() {
		this.runListener = false;
	}
	
	private void stopSpeaker() {
		this.runSpeaker = false;
	}
	
	private synchronized void stopEventually() {
		// call from listener thread only
		
		try {				
			threadSpeaker.join();
			connection.close();
//			System.out.println("stop connection");
			endMessaging.run();
		} catch (InterruptedException | IOException e) {
			e.printStackTrace();
		} finally {
			channel.stop();
			threadListener = null;
			threadSpeaker = null;
		}
	}
	
	public synchronized boolean isConnectionEstablished() {
		return connection != null && !connection.isClosed();
	}
	
	public void send(Message message) {
		outgoing.add(message);
	}
	
	public void send(LoaderMessage message) {
		outgoing.add(new LoaderMessageWrapper(message));
	}
	
	
	private static class StopMessage implements Message {
		private static final long serialVersionUID = -5579245300376539762L;
		private int messengerHashCode;
		
		public StopMessage(Messenger messenger) {
			this.messengerHashCode = messenger.hashCode();
		}
		
		private boolean isRequester(Messenger messenger) {
			return this.messengerHashCode == messenger.hashCode();
		}
		
		private boolean isReceiver(Messenger messenger) {
			return !isRequester(messenger);
		}

		@Override
		public void execute(Messenger messenger) {
			if(isReceiver(messenger)) {
				// stop receiver
				messenger.send(this);
				messenger.stopSpeaker();
				messenger.stopListener();
			} else if(isRequester(messenger)) {
				// stop requester
				messenger.stopListener();
			}
			
			messenger.stopEventually();
		}
	}
	
	private static class LoaderMessageWrapper implements Message {
		private static final long serialVersionUID = 2636770593396458480L;
		private LoaderMessage loaderMessage;
		
		public LoaderMessageWrapper(LoaderMessage loaderMessage) {
			this.loaderMessage = loaderMessage;
		}

		@Override
		public void execute(Messenger messenger) {
			loaderMessage.setMessenger(messenger);
			messenger.channel.getIncoming().add(loaderMessage);
		}
	}
}
