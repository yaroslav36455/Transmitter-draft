package ua.itea.model;

import java.io.Serializable;

public class RemoteFile extends FileHandler implements Serializable {
	private static final long serialVersionUID = -463906602128485756L;
	private final String name;
	private final FileSize fileSize;
	
	public RemoteFile(FileId fileId, FileSize fileSize, String name) {
		super(fileId);
		this.fileSize = fileSize;
		this.name = name;
	}

	public String getName() {
		return name;
	}

	@Override
	public FileSize getFileSize() {
		return fileSize;
	}
}
