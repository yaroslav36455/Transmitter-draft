package ua.itea.model;

public class MemorySize {
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
