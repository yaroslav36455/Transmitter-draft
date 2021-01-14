package ua.itea.model;

import java.io.File;

public class LocalFileRegistered extends FileHandler {
	private static final long serialVersionUID = -5110220119711321816L;
	private File file;
	private MemorySize size;
	
	public LocalFileRegistered(File file, MemorySize totalSize) {
		super(new FileId());
		this.file = file;
		size = totalSize;
	}

	public MemorySize getSize() {
		return size;
	}
	
	public File getFile() {
		return file;
	}
}
