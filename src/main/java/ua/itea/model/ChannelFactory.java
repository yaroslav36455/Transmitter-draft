package ua.itea.model;

public class ChannelFactory {
	private Downloader downloader;
	private Uploader uploader;
	
	public ChannelFactory(Downloader downloader, Uploader uploader) {
		this.downloader = downloader;
		this.uploader = uploader;
	}

	public Channel create(Connection connection) {
		Channel channel = new Channel();
		
		channel.setConnection(connection);
		channel.setDownloader(downloader);
		channel.setUploader(uploader);
		
		return channel;
	}

}
