package ua.itea.model;

import java.net.Socket;

public class ChannelProvider {

	private ChannelBase channelBase;
	private ChannelFactory channelFactory;
	
	public ChannelProvider(ChannelBase channelBase,
			               ChannelFactory channelFactory) {
		this.channelFactory = channelFactory;
		this.channelBase = channelBase;
	}

	public void establish(Socket socket) {
		Channel channel = selectChannel();
		
		if (channel == null) {
			channel = channelFactory.create();
			channelBase.add(channel);
		}
		
		channel.setSocket(socket);
		channel.start();
	}
	
	protected Channel selectChannel() {
		return null;
	}
}
