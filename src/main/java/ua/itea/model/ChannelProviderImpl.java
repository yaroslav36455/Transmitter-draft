package ua.itea.model;

import java.net.Socket;

public class ChannelProviderImpl implements ChannelProvider {

	private ChannelBase channelBase;
	private ChannelFactory channelFactory;
	
	public ChannelProviderImpl(ChannelBase channelBase,
			                   ChannelFactory channelFactory) {
		this.channelFactory = channelFactory;
		this.channelBase = channelBase;
	}

	@Override
	public void establish(Socket socket) {
		for (Channel channel : channelBase) {
			Socket cs = channel.getSocket();
			if (cs != null && !cs.isConnected()
					&& !cs.getInetAddress().equals(socket.getInetAddress())) {
				
				channel.setSocket(socket);
				channel.start();
				return;
			}
		}
		
		channelBase.add(channelFactory.create(socket));
	}
}
