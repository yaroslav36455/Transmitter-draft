package ua.itea.model;

import java.io.Serializable;

public class DataBlockInfo implements Serializable {
	private static final long serialVersionUID = -920107921661752297L;
	private final long offset;
	private final int size;
	
	public DataBlockInfo(long offset, int size) {
		this.offset = offset;
		this.size = size;
	}

	public long getOffset() {
		return offset;
	}

	public int getSize() {
		return size;
	}
}
