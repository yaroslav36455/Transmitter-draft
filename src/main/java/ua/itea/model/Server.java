package ua.itea.model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Server implements Runnable, AutoCloseable {
	private static final long KEY = (long) (Math.random() * Long.MAX_VALUE);
	private ServerSocket server;
	private ChannelProvider channelProvider;
	private int port;

	public Server(ChannelProvider channelProvider) {
		this.channelProvider = channelProvider;
	}

	public int getPort() {
		return port;
	}

	public void start(int port) throws IOException {
		server = new ServerSocket(port);
		this.port = port;
		new Thread(this).start();
	}

	@Override
	public void close() {
		try (Socket socket = new Socket(InetAddress.getByAddress(new byte[] { 127, 0, 0, 1 }),
									    server.getLocalPort());
				ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());) {

			oos.writeObject(Mark.STOP);
			oos.writeLong(KEY);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		boolean run = true;

		try {

			while (run) {
				Socket socket = server.accept();
				ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());

				switch (readBegin(ois)) {
				case START:
					channelProvider.establish(socket);
					break;

				case STOP:
					run = false;
					/* no break */
					
				case IGNORE:
				default:
					ois.close();
					socket.close();
					break;
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				server.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private Mark readBegin(ObjectInputStream ois) {
		Mark result = Mark.IGNORE;

		try {
			Mark command = (Mark) ois.readObject();
			long key = ois.readLong();

			if (command == Mark.STOP && key == KEY) {
				result = Mark.STOP;
			} else {
				result = command;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}
}
