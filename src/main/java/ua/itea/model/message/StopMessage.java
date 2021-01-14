package ua.itea.model.message;

import ua.itea.model.Messenger;

public class StopMessage implements Message {
	private int messengerHashCode;
	
	public StopMessage(Messenger messenger) {
		messengerHashCode = messenger.hashCode();
	}
	
	public boolean isRequester(Messenger messenger) {
		return messenger.hashCode() == messengerHashCode;
	}
	
	public boolean isReceiver(Messenger messenger) {
		return !isRequester(messenger);
	}
}
