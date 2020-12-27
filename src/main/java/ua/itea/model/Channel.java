package ua.itea.model;

import java.io.IOException;
import java.util.concurrent.Exchanger;
import java.util.concurrent.Semaphore;

public class Channel {
	private Connection connection;
	private Downloader downloader;
	private Uploader uploader;
	
	public Connection getConnection() {
		return connection;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
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
	
	public boolean isConnectionEstablished() {
		return connection != null && !connection.getSocket().isClosed();
	}

	public void start() {
		new Thread(listener).start();
		new Thread(handler).start();
	}
	
	public synchronized DataMessage createMessage(DataMessage incomingMessage) {
		// incoming
		DownloaderRequest downloaderRequest = incomingMessage.getDownloaderRequest();
		UploaderAnswer uploaderAnswer = incomingMessage.getUploaderAnswer();
		
		DataRequest downloaderDataRequest = downloaderRequest.getDataRequest();
		DataAnswer uloaderDataAnswer = uploaderAnswer.getDataAnswer();
		
		// outgoing
		DataAnswer outgoingDataAnswer = uploader.load(downloaderDataRequest);
		DataRequest outgoingDataRequest = downloader.load(uloaderDataAnswer);
		
		DataMessage outgoingMessage = new DataMessage();
		DownloaderRequest outgoingDownloaderRequest = new DownloaderRequest();
		UploaderAnswer outgoingUploaderAnswer = new UploaderAnswer();
		
		outgoingDownloaderRequest.setDataRequest(outgoingDataRequest);
		outgoingMessage.setDownloaderRequest(outgoingDownloaderRequest);
		
		outgoingUploaderAnswer.setDataAnswer(outgoingDataAnswer);
		outgoingMessage.setUploaderAnswer(outgoingUploaderAnswer);
		
		return outgoingMessage;
	}
	
	public void registerNewFiles(FileBase<RemoteFile> newFiles) throws InterruptedException {
		handler.notifyForNewFilesMessage();
		newFilesExchanger.exchange(newFiles);
	}
	
	private Listener listener = new Listener();
	private Handler handler = new Handler();
	private Exchanger<DataMessage> listenerDataMessageExchanger = new Exchanger<>();
	private Exchanger<FileBase<RemoteFile>> newFilesExchanger = new Exchanger<>();
	private Semaphore closingSemaphore = new Semaphore(1, true);
	
	public void stop() throws InterruptedException {
		handler.stopAsRequester();
	}
	
	private void closeConnection() {
		try {
			closingSemaphore.acquire();
			if (!connection.isClosed()) {
				connection.close();
			}
			closingSemaphore.release();
		} catch (IOException | InterruptedException ex) {
			ex.printStackTrace();
		}
	}
	
	private class Listener implements Runnable {
		private boolean run;
		
		@Override
		public void run() {
			run = true;
			
			try {				
				while (run) {
					switch (connection.readMark()) {
					case DATA:
						DataMessage incomingMessage = connection.readDataMessage();
						handler.notifyForDataMessage();
						listenerDataMessageExchanger.exchange(incomingMessage);
						break;
						
					case NEW_FILES:
						NewFilesMessage incomingFilesMessage = connection.readNewFileMessage();
						downloader.getRegistered().addAll(incomingFilesMessage.getFiles());
						break;

					case STOP_AS_RECEIVER:
						run = false;
						handler.stopAsReceiver();
						break;
						
					case STOP_AS_REQUESTER:
						run = false;
						closeConnection();
						break;
						
					default:
						break;
					}
				}	
			}catch(IOException | ClassNotFoundException | InterruptedException e) {
				closeConnection();
				e.printStackTrace();
			}
		}
	}
	
	private class Handler implements Runnable {
		private volatile Semaphore brace = new Semaphore(1, true);
		private volatile Semaphore markerSemaphore = new Semaphore(0, true);
		private volatile Mark mark;
		private volatile boolean run;
		
		@Override
		public void run() {
			run = true;
			
			try {				
				while (run) {
					markerSemaphore.acquire();
					
					switch (mark) {
					case DATA:
						DataMessage incomingMessage = listenerDataMessageExchanger.exchange(null);
						connection.writeDataMessage(createMessage(incomingMessage));
						break;
						
					case NEW_FILES:
						FileBase<RemoteFile> newFiles = newFilesExchanger.exchange(null);
						NewFilesMessage newFilesMessage = new NewFilesMessage();
						
						newFilesMessage.setFiles(newFiles);
						connection.writeNewFileMessage(newFilesMessage);
						break;
						
					case STOP_AS_REQUESTER:
						run = false;
						connection.stopReceiver();
						break;
						
					case STOP_AS_RECEIVER:
						run = false;
						connection.stopRequester();
						closeConnection();
						break;
						
					default:
						break;
					}
				}
			} catch(InterruptedException | IOException e) {
				closeConnection();
				e.printStackTrace();
			}
		}
		
		public void stopAsReceiver() throws InterruptedException {
			brace.acquire();
			mark = Mark.STOP_AS_RECEIVER;
			markerSemaphore.release();
			brace.release();
		}
		
		public void stopAsRequester() throws InterruptedException {
			brace.acquire();
			mark = Mark.STOP_AS_REQUESTER;
			markerSemaphore.release();
			brace.release();
		}

		public void notifyForDataMessage() throws InterruptedException {
			brace.acquire();
			mark = Mark.DATA;
			markerSemaphore.release();
			brace.release();
		}
		
		public void notifyForNewFilesMessage() throws InterruptedException {
			brace.acquire();
			mark = Mark.NEW_FILES;
			markerSemaphore.release();
			brace.release();
		}
	}
}
