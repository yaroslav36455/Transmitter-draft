package ua.itea.model;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

public class Server implements Runnable, AutoCloseable {
	private ServerSocket server;
	private ConnectionProvider connectionProvider;

	public Server(ConnectionProvider connectionProvider) {
		this.connectionProvider = connectionProvider;
	}

	public void start(int port) throws IOException {
		server = new ServerSocket(port);
		new Thread(this).start();
	}
	
	public int getLocalPort() {
		return server.getLocalPort();
	}

	@Override
	public void close() {

		try (Socket socket = new Socket(InetAddress.getByAddress(new byte[] { 127, 0, 0, 1 }),
										server.getLocalPort());
				ConnectionClient c = new ConnectionClient(socket)) {
			
			c.stopServer();
			c.writeKey();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		boolean run = true;

		while (run) {
			try {
				ConnectionServer c = new ConnectionServer(server.accept());
				
				switch (c.readMark()) {
				case START:
					 connectionProvider.start(c);
					break;

				case STOP_SERVER:
					if (Connection.isValidKey(c.readKey())) {
						run = false;	
					}
					/* no break */
					
				case IGNORE:
				default:
					c.close();
					break;
				}
			} catch (IOException | ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		
		try {
			server.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
