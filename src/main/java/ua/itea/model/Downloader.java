package ua.itea.model;

import java.io.IOException;

public class Downloader {
	private FileBase<RemoteFile> registered;
	private FileBase<LocalFileWriteable> files;
	private LoadLimit loadLimit;
	
	public Downloader() {
		this.loadLimit = new LoadLimit(50);
	}

	public FileBase<RemoteFile> getRegistered() {
		return registered;
	}

	public void setRegistered(FileBase<RemoteFile> registered) {
		this.registered = registered;
	}

	public FileBase<LocalFileWriteable> getFiles() {
		return files;
	}

	public void setFiles(FileBase<LocalFileWriteable> files) {
		this.files = files;
	}

	public LoadLimit getLoadLimit() {
		return loadLimit;
	}

	public void setLoadLimit(LoadLimit downloadLimit) {
		this.loadLimit = downloadLimit;
	}

	public DataRequest load(DataAnswer dataAnswer) {
		DataRequest dataRequest = null;
		
		try {
			for (DataFileAnswer dataFileAnswer : dataAnswer) {
				LocalFileWriteable file = files.getFile(dataFileAnswer.getFileId());
				file.write(dataFileAnswer.getDataBlock());
			}
			
			Priority totalPriority = totalPriotity();
			
			for (LocalFileWriteable file : files) {
				if (!file.isCompleted()) {
					float percent = file.getPriority().percent(totalPriority);
					int maxAllowed = (int) percent * loadLimit.getLimint();
					DataBlockInfo dataBlockInfo = file.createRequest(maxAllowed);
					
					if (dataRequest == null) {
						dataRequest = new DataRequest();
					}
					dataRequest.add(new DataFileRequest(file.getFileId(), dataBlockInfo));
				}
			}	
		} catch (IOException e) {
			e.printStackTrace();
			dataRequest = null;
		}
		
		return dataRequest;
	}
	
	private Priority totalPriotity() {
		int totalPriority = 0;
		
		for (LocalFileWriteable localFileWritable : files) {
			totalPriority += localFileWritable.getPriority().get();
		}
		
		return new Priority(totalPriority);
	}
}
