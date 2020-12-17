package ua.itea.model;

public class Client {
	private ChannelProvider channelProvider;
	private SocketFactory socketFactory;
	
	public Client(ChannelProvider channelProvider) {
		this.channelProvider = channelProvider;
	}
	
	public void createChannel() {
		channelProvider.create(socketFactory.create());
	}
}
