package ua.itea.model;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.BindException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;

import javafx.util.Callback;

public class Server implements Runnable {
	private static final long KEY = 1;
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
	
	public void stop() {	
		try (Socket socket = new Socket(InetAddress.getByAddress(new byte[] {127, 0, 0, 1}),
										server.getLocalPort());
			 ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());) {
			
			oos.writeObject(Command.STOP);
			oos.writeLong(KEY);

		} catch (IOException e) {	
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		boolean run = true;
		
		try {
			
			while(run) {
				System.out.println("server " + server.getLocalPort() + " waits");
				
				Socket socket = server.accept();
				ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
				
				switch (read(ois)) {
				case START:
					channelProvider.establish(socket);	
					break;
					
				case STOP:
					System.out.println("server " + server.getLocalPort() + " stop");
					run = false;
					break;
					
				case IGNORE:
					System.out.println("bad command");
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
		
		System.out.println("run end");
	}
	
	private Command read(ObjectInputStream ois) {
		Command result = Command.IGNORE;
		
		try {
			Command command = (Command) ois.readObject();
			long key = ois.readLong();
			
			if (command == Command.STOP
					&& key == KEY) {
				result = Command.STOP;
			} else {
				result = command;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	private static enum Command implements Serializable {
		START, STOP, IGNORE
	}
}
