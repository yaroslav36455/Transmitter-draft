package ua.itea.config;

import ua.itea.model.LoadLimit;

public class Config {
	private String downloadDirectory;
	private LoadLimit uploadLimit;
	private LoadLimit downloadLimit;

	public Config() {
		this.downloadDirectory = "user/download/";
		this.uploadLimit = new LoadLimit(500);
		this.downloadLimit = new LoadLimit(500);
	}

	public String getDownloadDirectory() {
		return downloadDirectory;
	}

	public LoadLimit getUploadLimit() {
		return uploadLimit;
	}

	public LoadLimit getDownloadLimit() {
		return downloadLimit;
	}
}
