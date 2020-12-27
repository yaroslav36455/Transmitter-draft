package ua.itea.config;

public class Config {
	private String downloadDirectory;

	public Config() {
		this.downloadDirectory = "user/download/";
	}

	public String getDownloadDirectory() {
		return downloadDirectory;
	}
}
