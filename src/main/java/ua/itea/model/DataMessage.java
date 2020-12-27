package ua.itea.model;

import java.io.Serializable;

public class DataMessage implements Serializable {
	private static final long serialVersionUID = 4623194330617498635L;
	private DownloaderRequest downloaderRequest;
	private UploaderAnswer uploaderAnswer;
	
	public DownloaderRequest getDownloaderRequest() {
		return downloaderRequest;
	}
	
	public void setDownloaderRequest(DownloaderRequest downloaderRequest) {
		this.downloaderRequest = downloaderRequest;
	}
	
	public UploaderAnswer getUploaderAnswer() {
		return uploaderAnswer;
	}
	
	public void setUploaderAnswer(UploaderAnswer uploaderAnswer) {
		this.uploaderAnswer = uploaderAnswer;
	}
}
