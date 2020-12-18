package ua.itea.model;

import java.io.IOException;

public class Writer {
	private FileBase<LocalFileWriteable> fileBase;
	private LoadLimit downloadLimit;
	
	public FileBase<LocalFileWriteable> getFileBase() {
		return fileBase;
	}

	public void setFileBase(FileBase<LocalFileWriteable> fileBase) {
		this.fileBase = fileBase;
	}

	public LoadLimit getDownloadLimit() {
		return downloadLimit;
	}

	public void setDownloadLimit(LoadLimit downloadLimit) {
		this.downloadLimit = downloadLimit;
	}

	public void process(DataAnswer dataAnswer) throws IOException {
		for (DataFileAnswer dataFileAnswer : dataAnswer) {
			LocalFileWriteable file = fileBase.getFile(dataFileAnswer.getFileId());
			file.write(dataFileAnswer.getDataBlock());
		}
	}
	
	public DataRequest createRequest() throws IOException {
		Priority totalPriority = totalPriotity();
		DataRequest dataRequest = null;
		
		for (LocalFileWriteable file : fileBase) {
			if (!file.isCompleted()) {
				float percent = file.getPriority().percent(totalPriority);
				int maxAllowed = (int) percent * downloadLimit.getLimint();
				DataBlockInfo dataBlockInfo = file.createRequest(maxAllowed);
				
				if (dataRequest == null) {
					dataRequest = new DataRequest();
				}
				dataRequest.add(new DataFileRequest(file.getFileId(), dataBlockInfo));
			}
		}
		
		return dataRequest;
	}
	
	private Priority totalPriotity() {
		int totalPriority = 0;
		
		for (LocalFileWriteable localFileWritable : fileBase) {
			totalPriority += localFileWritable.getPriority().get();
		}
		
		return new Priority(totalPriority);
	}
}
