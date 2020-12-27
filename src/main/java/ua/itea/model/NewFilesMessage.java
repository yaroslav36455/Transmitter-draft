package ua.itea.model;

import java.io.Serializable;

public class NewFilesMessage implements Serializable {
	private static final long serialVersionUID = -4566207102279856532L;
	private FileBase<RemoteFile> files;

	public FileBase<RemoteFile> getFiles() {
		return files;
	}

	public void setFiles(FileBase<RemoteFile> files) {
		this.files = files;
	}
}
