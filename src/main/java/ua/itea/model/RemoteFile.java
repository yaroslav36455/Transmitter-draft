package ua.itea.model;

public class RemoteFile extends FileHandler {
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
