package ua.itea.model;

import java.io.IOException;
import java.net.UnknownHostException;

public class Client {
	private ChannelProvider channelProvider;
	private SocketFactory socketFactory;
	
	public Client(ChannelProvider channelProvider, SocketFactory socketFactory) {
		this.channelProvider = channelProvider;
		this.socketFactory = socketFactory;
	}
	
	public void createChannel() throws UnknownHostException, IOException {
		channelProvider.establish(socketFactory.create());
	}
}
