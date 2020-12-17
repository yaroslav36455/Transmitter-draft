package ua.itea.model;

import java.io.File;

public abstract class LocalFile extends FileHandler {
	private final File file;
	
	public LocalFile(FileId fileId, File file) {
		super(fileId);
		this.file = file;
	}

	public File getFile() {
		return file;
	}
}
