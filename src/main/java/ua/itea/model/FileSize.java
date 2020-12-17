package ua.itea.model;

import java.io.Serializable;

public class FileSize implements Serializable {
	private static final long serialVersionUID = -7187973843919065688L;
	private final long size;
	private final long totalSize;
	
	public FileSize(long size, long totalSize) {
		this.size = size;
		this.totalSize = totalSize;
	}

	public long getFilledSize() {
		return size;
	}
	
	public long getTotalSize() {
		return totalSize;
	}
}
