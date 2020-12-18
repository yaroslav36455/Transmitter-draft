package ua.itea.model;

import java.io.IOException;
import java.net.ServerSocket;

public class Server implements Runnable {
	private int port;
	private ChannelProvider channelProvider;

	public Server(int port, ChannelProvider channelProvider) {
		this.port = port;
		this.channelProvider = channelProvider;
		
		Thread thread = new Thread(this);
		thread.setDaemon(true);
		thread.start();
	}

	@Override
	public void run() {
		try (ServerSocket server = new ServerSocket(port)) {
			
			while(true) {
				System.out.println("server waits");
				channelProvider.establish(server.accept());
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}	
}
