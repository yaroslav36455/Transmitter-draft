package ua.itea.model;

import java.io.IOException;

public class Uploader {
	private FileBase<LocalFileReadable> files;
	private LoadLimit loadLimit;
	
	public Uploader(FileBase<LocalFileReadable> files) {
		this();
		this.files = files;
	}
	
	public Uploader() {
		loadLimit = new LoadLimit(50);
	}
	
	public FileBase<LocalFileReadable> getFiles() {
		return files;
	}

	public void setFiles(FileBase<LocalFileReadable> files) {
		this.files = files;
	}
	
	public LoadLimit getLoadLimit() {
		return loadLimit;
	}

	public void setLoadLimit(LoadLimit loadLimit) {
		this.loadLimit = loadLimit;
	}
	
	public DataAnswer load(DataRequest dataRequest) {
		DataAnswer dataAnswer = null;
		
		try {
			for (DataFileRequest dataFileRequest : dataRequest) {
				if (dataAnswer == null) {
					dataAnswer = new DataAnswer();
				}			
				dataAnswer.add(load(dataFileRequest));
			}	
		} catch (IOException e) {
			e.printStackTrace();
			dataAnswer = null;
		}
		
		return dataAnswer;
	}
	
	private DataFileAnswer load(DataFileRequest dataFileRequest) throws IOException {
		LocalFileReadable file = files.getFile(dataFileRequest.getFileId());
		DataBlock dataBlock = file.read(dataFileRequest.getDataBlockInfo());
		
		return new DataFileAnswer(dataFileRequest.getFileId(), dataBlock);
	}
}
