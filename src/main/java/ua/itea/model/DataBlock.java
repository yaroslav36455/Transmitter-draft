package ua.itea.model;

import java.io.Serializable;

public class DataBlock implements Serializable {
	private static final long serialVersionUID = 6695452283249024545L;
	private final long offset;
	private final byte[] data;
	
	public DataBlock(long offset, byte[] data) {
		this.offset = offset;
		this.data = data;
	}
	
	public long getOffset() {
		return offset;
	}
	
	public int getSize() {
		return data.length;
	}
	
	public byte[] getData() {
		return data;
	}
}
