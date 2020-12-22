package ua.itea.model;

import java.io.File;
import java.io.IOException;
import java.net.Socket;

public class ClientChannelFactory implements ChannelFactory {

	@Override
	public Channel create() {
		Channel channel = new ClientChannel();
		Downloader downloader = new Downloader();
		Uploader uploader = new Uploader();
		
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
		
		downloader.setFiles(writable);
		uploader.setFiles(readable);
		channel.setDownloader(downloader);
		channel.setUploader(uploader);
		
		return channel;
	}

}
