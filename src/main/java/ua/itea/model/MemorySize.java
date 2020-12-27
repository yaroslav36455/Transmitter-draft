package ua.itea.model;

import java.io.Serializable;

public class MemorySize implements Serializable {
	private static final long serialVersionUID = 4278915701923881980L;
	private final long size;

	public MemorySize(long size) {
		this.size = size;
	}

	public long getSize() {
		return size;
	}
	
	@Override
	public String toString() {
		return String.valueOf(size);
	}
}
