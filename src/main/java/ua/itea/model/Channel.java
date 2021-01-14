package ua.itea.model;

import java.io.File;
import java.util.List;

import ua.itea.model.factory.LocalFileReadableFactory;
import ua.itea.model.factory.RemoteFileRegisteredFactory;
import ua.itea.model.message.AutoBlockingQueue;
import ua.itea.model.message.DistributorIncoming;
import ua.itea.model.message.Message;
import ua.itea.model.message.factory.NewFilesMessageFactory;
import ua.itea.model.message.factory.RemoveFilesMessageFactory;

public class Channel {
	private Loader loader;
	private Messenger messenger;
	private DistributorIncoming distributorIncoming;
	private AutoBlockingQueue<Message> incomingQueue;
	private AutoBlockingQueue<Message> outgoingQueue;
	
	private boolean run; 
	
	public Channel() {
		incomingQueue = new AutoBlockingQueue<>();
		outgoingQueue = new AutoBlockingQueue<>();
		
		distributorIncoming = new DistributorIncoming();
		distributorIncoming.setIncomingQueue(incomingQueue);
		distributorIncoming.setLoaderQueue(new AutoBlockingQueue<>());
	}

	public Loader getLoader() {
		return loader;
	}

	public void setLoader(Loader loader) {
		this.loader = loader;
		this.loader.setIncoming(distributorIncoming.getLoaderQueue());
		this.loader.setOutgoing(outgoingQueue);
		
		Uploader.Registered ur = this.loader.getUploader().getRegistered();
		ur.setNewFilesMessageFactory(new NewFilesMessageFactory());
		ur.setRemoveFilesMessageFactory(new RemoveFilesMessageFactory());
		ur.setRemoteFileRegisteredFactory(new RemoteFileRegisteredFactory());
	}
	
	public Messenger getMessenger() {
		return messenger;
	}
	
	public void setMessenger(Messenger messenger) {
		this.messenger = messenger;
		this.messenger.setIncomingQueue(incomingQueue);
		this.messenger.setOutgoingQueue(outgoingQueue);
	}
	
	public synchronized void start() {
		loader.start();
		distributorIncoming.start();
		messenger.start();
		
		Uploader.Registered registered = getLoader().getUploader().getRegistered();
		registered.updateRemote();
		
		run = true;
	}
	
	public synchronized void stop() {
		messenger.stop();
		distributorIncoming.stop();
		loader.stop();
		
		Uploader.Registered registered = getLoader().getUploader().getRegistered();
		registered.resetRemote();
		
		run = false;
	}
	
	public boolean isRunning() {
		return run;
	}

	public synchronized void registerFiles(List<File> files) {		
		 getLoader().getUploader().addAll(files);
		
		if (messenger.isConnectionEstablished()) {
			getLoader().getUploader().getRegistered().updateRemote();
		}
	}
}
