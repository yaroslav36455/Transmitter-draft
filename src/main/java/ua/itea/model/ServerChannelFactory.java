package ua.itea.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.Socket;

public class ServerChannelFactory implements ChannelFactory {

	@Override
	public Channel create(Socket socket) {
		Channel channel = new Channel();
		LocalFileBase localFileBase = new LocalFileBase();
		FileBase<LocalFileWriteable> writable = new FileBase<>();
		FileBase<LocalFileReadable> readable = new FileBase<>();
		
		String filePath = "/home/vessel/Documents/eclipse-java-workspace/Transmitter/server/3746348.jpg";
		try {
			readable.add(new LocalFileReadable(new File(filePath)));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		localFileBase.setWriteableBase(writable);
		localFileBase.setReadableBase(readable);
		channel.setLocalFileBase(localFileBase);
		
		channel.setSocket(socket);
		channel.start(true);
		
		return channel;
	}

}
