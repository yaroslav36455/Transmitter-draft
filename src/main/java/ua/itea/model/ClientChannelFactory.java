package ua.itea.model;

import java.io.File;
import java.io.IOException;
import java.net.Socket;

public class ClientChannelFactory implements ChannelFactory {

	@Override
	public Channel create(Socket socket) {
		Channel channel = new ClientChannel();
		LocalFileBase localFileBase = new LocalFileBase();
		FileBase<LocalFileWriteable> writable = new FileBase<>();
		FileBase<LocalFileReadable> readable = new FileBase<>();
		
		String filePath = "/home/vessel/Documents/eclipse-java-workspace/Transmitter/client/3746348.jpg";
		WriteableFileInfo writeableFileInfo = new WriteableFileInfo(new FileId(),
				new File(filePath), new FileTotalSize(115827), new Priority(1));
		try {
			writable.add(new LocalFileWriteable(writeableFileInfo));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		localFileBase.setWriteableBase(writable);
		localFileBase.setReadableBase(readable);
		channel.setLocalFileBase(localFileBase);
		
		channel.setSocket(socket);
		channel.start();
		
		return channel;
	}

}
