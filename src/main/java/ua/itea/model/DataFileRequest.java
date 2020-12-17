package ua.itea.model;

import java.io.Serializable;

public class DataFileRequest implements Serializable {
	private static final long serialVersionUID = 7070583095831403142L;
	private final FileId fileId;
	private final DataBlockInfo dataBlockInfo;
	
	public DataFileRequest(FileId fileId, DataBlockInfo dataBlockInfo) {
		this.fileId = fileId;
		this.dataBlockInfo = dataBlockInfo;
	}

	public FileId getFileId() {
		return fileId;
	}

	public DataBlockInfo getDataBlockInfo() {
		return dataBlockInfo;
	}
}
