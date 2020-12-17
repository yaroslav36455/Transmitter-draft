package ua.itea.model;

import java.io.IOException;

public class Writer {
	private FileBase<LocalFileWritable> fileBase;
	private LoadLimit downloadLimit;
	
	public Writer(FileBase<LocalFileWritable> fileBase, LoadLimit downloadLimit) {
		this.fileBase = fileBase;
		this.downloadLimit = downloadLimit;
	}
	
	public LoadLimit getDownloadLimit() {
		return downloadLimit;
	}

	public void setDownloadLimit(LoadLimit downloadLimit) {
		this.downloadLimit = downloadLimit;
	}

	public void process(DataAnswer dataAnswer) throws IOException {
		for (DataFileAnswer dataFileAnswer : dataAnswer) {
			LocalFileWritable file = fileBase.getFile(dataFileAnswer.getFileId());
			file.write(dataFileAnswer.getDataBlock());
		}
	}
	
	public DataRequest createRequest() throws IOException {
		Priority totalPriority = totalPriotity();
		DataRequest dataRequest = new DataRequest();
		
		for (LocalFileWritable file : fileBase) {
			if (!file.isCompleted()) {
				float percent = file.getPriority().percent(totalPriority);
				int maxAllowed = (int) percent * downloadLimit.getLimint();
				DataBlockInfo dataBlockInfo = file.createRequest(maxAllowed);
				
				dataRequest.add(new DataFileRequest(file.getFileId(), dataBlockInfo));
			}
		}
		
		return dataRequest;
	}
	
	private Priority totalPriotity() {
		int totalPriority = 0;
		
		for (LocalFileWritable localFileWritable : fileBase) {
			totalPriority += localFileWritable.getPriority().get();
		}
		
		return new Priority(totalPriority);
	}
}
