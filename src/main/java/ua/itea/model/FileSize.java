package ua.itea.model;

import java.io.Serializable;

public class FileSize implements Serializable {
	private static final long serialVersionUID = -7187973843919065688L;
	private MemorySize filledSize;
	private MemorySize totalSize;
	
	public FileSize(long filledSize, long totalSize) {
		this.filledSize = new MemorySize(filledSize);
		this.totalSize = new MemorySize(totalSize);
	}
	
	public FileSize(MemorySize filledSize, MemorySize totalSize) {
		this.filledSize = filledSize;
		this.totalSize = totalSize;
	}

	public MemorySize getFilledSize() {
		return filledSize;
	}
	
	public MemorySize getTotalSize() {
		return totalSize;
	}
}
