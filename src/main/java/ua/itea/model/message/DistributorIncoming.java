package ua.itea.model.message;

public class DistributorIncoming {
	private AutoBlockingQueue<Message> incomingQueue;
	private AutoBlockingQueue<LoaderMessage> loaderQueue;
	private Thread thread;
	
	public synchronized void start() {
		thread = new Thread(this::loop);
		thread.start();
	}
	
	public synchronized void stop() {
		thread.interrupt();
		thread = null;
	}
	
	private void loop() {
		try {
			while(true) {
				Message message = incomingQueue.poll();
				
				if (message instanceof LoaderMessage) {
					loaderQueue.add((LoaderMessage) message);
				}
			}	
		} catch (InterruptedException e) {
//			e.printStackTrace();
		}
	}

	public AutoBlockingQueue<Message> getIncomingQueue() {
		return incomingQueue;
	}

	public void setIncomingQueue(AutoBlockingQueue<Message> incomingQueue) {
		this.incomingQueue = incomingQueue;
	}

	public AutoBlockingQueue<LoaderMessage> getLoaderQueue() {
		return loaderQueue;
	}

	public void setLoaderQueue(AutoBlockingQueue<LoaderMessage> loaderQueue) {
		this.loaderQueue = loaderQueue;
	}
}
