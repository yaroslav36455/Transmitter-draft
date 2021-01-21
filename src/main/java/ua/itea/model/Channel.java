package ua.itea.model;

import java.io.File;
import java.util.List;

import ua.itea.model.factory.RemoteFileRegisteredFactory;
import ua.itea.model.message.AutoBlockingQueue;
import ua.itea.model.message.LoaderMessage;
import ua.itea.model.message.factory.NewFilesMessageFactory;
import ua.itea.model.message.factory.RemoveFilesMessageFactory;

public class Channel {
	private boolean run;
	private Downloader downloader;
	private Uploader uploader;
	private AutoBlockingQueue<LoaderMessage> incoming;
	private Thread thread;
	
	public synchronized void registerFiles(List<File> files, Messenger messenger) {		
		uploader.addAll(files);
		uploader.updateRemote(messenger);
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
		
		Uploader.Registered ur = uploader.getRegistered();
		ur.setNewFilesMessageFactory(new NewFilesMessageFactory());
		ur.setRemoveFilesMessageFactory(new RemoveFilesMessageFactory());
		ur.setRemoteFileRegisteredFactory(new RemoteFileRegisteredFactory());
	}

	public AutoBlockingQueue<LoaderMessage> getIncoming() {
		return incoming;
	}

	public void setIncoming(AutoBlockingQueue<LoaderMessage> incoming) {
		this.incoming = incoming;
	}

	private void loop() {
		run = true;

		try {
			while (run) {
				LoaderMessage incomingMessage = incoming.poll();
				incomingMessage.execute(this);
			}
		} catch (InterruptedException e) {
//			e.printStackTrace();
		}
	}

	public synchronized void start(Messenger messenger) {
		thread = new Thread(this::loop);
		thread.start();

		uploader.start(messenger);
	}

	public synchronized void stop() {
		if (thread != null) {
			thread.interrupt();
			thread = null;

			uploader.stop();
			downloader.stop();
		}
	}
}
