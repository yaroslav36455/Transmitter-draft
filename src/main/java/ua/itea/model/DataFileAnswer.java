package ua.itea.model;

import java.io.Serializable;

public class DataFileAnswer implements Serializable, Comparable<DataFileAnswer> {
	private static final long serialVersionUID = 5333341124937282021L;
	private final FileId fileId;
	private final DataBlock dataBlock;
	
	public DataFileAnswer(FileId fileId, DataBlock dataBlock) {
		this.fileId = fileId;
		this.dataBlock = dataBlock;
	}
	
	public FileId getFileId() {
		return fileId;
	}

	public DataBlock getDataBlock() {
		return dataBlock;
	}

	@Override
	public int compareTo(DataFileAnswer o) {
		return fileId.get() - o.getFileId().get();
	}
}
