package ua.itea.model;

import java.io.IOException;
import java.net.BindException;

public class ServerFactory {
	private static final int LOWER_BOUND_PORT = 1024;
	private static final int RANGE_PORT = 49152 - LOWER_BOUND_PORT;
	private ConnectionProvider connectionProvider;

	public ServerFactory(ConnectionProvider connectionProvider) {
		this.connectionProvider = connectionProvider;
	}

	public Server create() throws IOException {
		final int ATTEMPTS = 100;
		Server server = new Server(connectionProvider);

		boolean created = false;
		int port = 0;
		for (int i = 0; i < ATTEMPTS && !created; i++) {
			try {
				port = LOWER_BOUND_PORT + (int) (Math.random() * RANGE_PORT);
				server.start(port);
				created = true;
			} catch (BindException ex) {
				System.out.println(port + " exists, try again");
			}
		}
		
		if (!created) {
			throw new BindException();
		}
		
		return server;
	}

}
