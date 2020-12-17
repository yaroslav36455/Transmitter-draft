package ua.itea.model;

import java.io.IOException;
import java.io.Serializable;

public abstract class FileHandler implements Serializable {
	private static final long serialVersionUID = -8540875129603292780L;
	private final FileId fileId;
	
	public FileHandler(FileId fileId) {
		this.fileId = fileId;
	}

	public FileId getFileId() {
		return fileId;
	}
	
	public abstract FileSize getFileSize() throws IOException;
}
