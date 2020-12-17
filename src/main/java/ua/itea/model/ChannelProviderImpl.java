package ua.itea.model;

import java.net.Socket;

public class ChannelProviderImpl implements ChannelProvider {
	private ChannelBase channelBase;
	private LocalFileBaseProvider localFileBaseProvider;
	
	@Override
	public void create(Socket socket) {
		Channel channel = new Channel(socket, localFileBaseProvider.get());
		
		channelBase.add(channel);
	}

}
