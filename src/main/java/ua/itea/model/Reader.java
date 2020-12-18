package ua.itea.model;

import java.io.IOException;

public class Reader {
	private FileBase<LocalFileReadable> fileBase;

	public FileBase<LocalFileReadable> getFileBase() {
		return fileBase;
	}

	public void setFileBase(FileBase<LocalFileReadable> fileBase) {
		this.fileBase = fileBase;
	}

	public DataAnswer process(DataRequest dataRequest) throws IOException {
		DataAnswer dataAnswer = new DataAnswer();
		
		for (DataFileRequest dataFileRequest : dataRequest) {
			dataAnswer.add(process(dataFileRequest));
		}
		
		return dataAnswer;
	}

	private DataFileAnswer process(DataFileRequest dataFileRequest) throws IOException {
		LocalFileReadable file = fileBase.getFile(dataFileRequest.getFileId());
		DataBlock dataBlock = file.read(dataFileRequest.getDataBlockInfo());
		
		return new DataFileAnswer(dataFileRequest.getFileId(), dataBlock);
	}
}
