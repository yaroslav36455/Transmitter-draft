package ua.itea.model.message;

import ua.itea.model.Channel;
import ua.itea.model.Messenger;

public abstract class LoaderMessage {
	private Messenger messenger;
	
	public Messenger getMessenger() {
		return messenger;
	}

	public void setMessenger(Messenger messenger) {
		this.messenger = messenger;
	}

	public abstract void execute(Channel loader);
}
