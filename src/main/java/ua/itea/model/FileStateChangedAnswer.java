package ua.itea.model;

import java.io.Serializable;

public class FileStateChangedAnswer implements Serializable {
	private static final long serialVersionUID = 5925463816378120303L;
	private final FileId fileId;

	public FileStateChangedAnswer(FileId fileId) {
		this.fileId = fileId;
	}

	public FileId getFileId() {
		return fileId;
	}
}
