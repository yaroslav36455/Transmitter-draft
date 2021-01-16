package ua.itea.model;

import ua.itea.model.message.AutoBlockingQueue;
import ua.itea.model.message.DataAnswer;
import ua.itea.model.message.DataMessage;
import ua.itea.model.message.DataRequest;
import ua.itea.model.message.LoaderMessage;
import ua.itea.model.message.Message;
import ua.itea.model.message.NewFilesMessage;
import ua.itea.model.message.RemoveFilesMessage;
import ua.itea.model.message.factory.DataMessageFactory;

public class Loader {
	private boolean run;
	private Downloader downloader;
	private Uploader uploader;
	private AutoBlockingQueue<LoaderMessage> incoming;
	private AutoBlockingQueue<Message> outgoing;
	private DataMessageFactory messageFactory;
	private Thread thread;

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

	public DataMessageFactory getMessageFactory() {
		return messageFactory;
	}

	public void setMessageFactory(DataMessageFactory messageFactory) {
		this.messageFactory = messageFactory;
	}

	public AutoBlockingQueue<LoaderMessage> getIncoming() {
		return incoming;
	}

	public void setIncoming(AutoBlockingQueue<LoaderMessage> incoming) {
		this.incoming = incoming;
	}

	public AutoBlockingQueue<Message> getOutgoing() {
		return outgoing;
	}

	public void setOutgoing(AutoBlockingQueue<Message> outgoing) {
		this.outgoing = outgoing;
		this.getUploader().getRegistered().setOutgoing(outgoing);
	}

	private void loop() {
		run = true;

		try {
			while (run) {
				LoaderMessage incomingMessage = incoming.poll();
				
				if (incomingMessage instanceof DataMessage) {
					DataMessage outgoingMessage = process((DataMessage) incomingMessage);
					
					if (!outgoingMessage.isEmpty()) {
						outgoing.add(outgoingMessage);
					}
					
				} else if (incomingMessage instanceof NewFilesMessage) {
					addNewFiles((NewFilesMessage) incomingMessage);
				} else if (incomingMessage instanceof RemoveFilesMessage) {
					removeFiles((RemoveFilesMessage) incomingMessage);
				}
			}
		} catch (InterruptedException e) {
//			e.printStackTrace();
		}
	}

	private DataMessage process(DataMessage incomingMessage) {
		if(incomingMessage.getDataAnswer() != null) {
			downloader.save(incomingMessage.getDataAnswer());	
		}
		return createMessage(incomingMessage);
	}
	
	private DataMessage createMessage(DataMessage incomingMessage) {
		DataMessage message = messageFactory.create();
		DataRequest request = downloader.createRequest();
		DataAnswer answer = null;
		if(incomingMessage.getDataRequest() != null) {
			answer = uploader.load(incomingMessage.getDataRequest());	
		}
		
		message.setDataAnswer(answer);
		message.setDataRequest(request);
		return message;
	}

	public synchronized void start() {
		thread = new Thread(this::loop);
		thread.start();
	}
	
	public synchronized void stop() {
		if (thread != null) {
			thread.interrupt();
			thread = null;
		}
	}
	
	private void addNewFiles(NewFilesMessage newFilesMessage) {
		downloader.addAll(newFilesMessage.getList());
	}
	
	private void removeFiles(RemoveFilesMessage removeFilesMessage) {
		downloader.removeAll(removeFilesMessage.getList());
	}
}
