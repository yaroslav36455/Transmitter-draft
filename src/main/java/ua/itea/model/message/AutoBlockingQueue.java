package ua.itea.model.message;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.Semaphore;

public class AutoBlockingQueue<T> {
	private Semaphore semaphore;
	private Queue<T> messageQueue;
	
	public AutoBlockingQueue() {
		messageQueue = new ConcurrentLinkedDeque<>();
		semaphore = new Semaphore(messageQueue.size(), true);
	}

	public void add(T elem) {
		messageQueue.add(elem);
		semaphore.release();
	}
	
	public T poll() throws InterruptedException {
		semaphore.acquire();
		return messageQueue.poll();
	}
}
