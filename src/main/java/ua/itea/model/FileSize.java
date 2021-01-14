package ua.itea.model;

import java.io.Serializable;

public class FileSize implements Serializable {
	private static final long serialVersionUID = -7187973843919065688L;
	private MemorySize filledSize;
	private FileTotalSize totalSize;
	
	public FileSize(long filledSize, long totalSize) {
		this.filledSize = new MemorySize(filledSize);
		this.totalSize = new FileTotalSize(totalSize);
	}
	
	public FileSize(MemorySize filledSize, FileTotalSize totalSize) {
		this.filledSize = filledSize;
		this.totalSize = totalSize;
	}
	
	public FileSize(FileTotalSize totalSize) {
		this.filledSize = new MemorySize();
		this.totalSize = totalSize;
	}

	public MemorySize getFilledSize() {
		return filledSize;
	}
	
	public FileTotalSize getTotalSize() {
		return totalSize;
	}
}
