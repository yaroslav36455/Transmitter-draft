package ua.itea.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.Socket;

public class ServerChannelFactory implements ChannelFactory {
	
	@Override
	public Channel create() {
		Channel channel = new ServerChannel();
		Downloader downloader = new Downloader();
		Uploader uploader = new Uploader();

		FileBase<LocalFileWriteable> writable = new FileBase<>();
		FileBase<LocalFileReadable> readable = new FileBase<>();
		
		String filePath = "/home/vessel/Documents/eclipse-java-workspace/Transmitter/server/3746348.jpg";
		try {
			readable.add(new LocalFileReadable(new File(filePath)));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		downloader.setFiles(writable);
		uploader.setFiles(readable);
		channel.setDownloader(downloader);
		channel.setUploader(uploader);
		
		return channel;
	}

}
