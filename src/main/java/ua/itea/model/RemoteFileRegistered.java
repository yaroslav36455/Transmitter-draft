package ua.itea.model;

public class RemoteFileRegistered extends FileHandler {
	private static final long serialVersionUID = 5033784211680456523L;
	private String name;
	private MemorySize size;

	public RemoteFileRegistered(FileId fileId, String name,
								MemorySize size) {
		super(fileId);
		this.name = name;
		this.size = size;
	}
	
	public String getName() {
		return name;
	}

	public MemorySize getSize() {
		return size;
	}
}
