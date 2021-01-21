package ua.itea.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ua.itea.model.factory.LocalFileWriteableFactory;

public class Downloader {
	private LocalFileWriteableFactory localFileWriteableFactory;
	private FileBase<LocalFileWriteable> files;
	private LoadLimit downloadLimit;
	private LoadLimit remoteUploadLimit;

	public LocalFileWriteableFactory getLocalFileWriteableFactory() {
		return localFileWriteableFactory;
	}

	public void setLocalFileWriteableFactory(LocalFileWriteableFactory localFileWriteableFactory) {
		this.localFileWriteableFactory = localFileWriteableFactory;
	}

	public FileBase<LocalFileWriteable> getFiles() {
		return files;
	}

	public void setFiles(FileBase<LocalFileWriteable> files) {
		this.files = files;
	}

	public LoadLimit getLoadLimit() {
		return downloadLimit;
	}

	public void setLoadLimit(LoadLimit downloadLimit) {
		this.downloadLimit = downloadLimit;
	}
	
	public void stop() {
		closeAll();
	}
	
	private void closeAll() {
		for (LocalFileWriteable localFileWriteable : files) {
			try {
				if (localFileWriteable.isOpened()) {
					localFileWriteable.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void beginDownloading(List<FileId> idList) throws IOException {
		for (FileId fileId : idList) {
			files.getFile(fileId).open();
		}
	}

	public void save(List<DataFileAnswer> dataFileAnswers) {
		try {
			for (DataFileAnswer dataFileAnswer : dataFileAnswers) {
				LocalFileWriteable file = files.getFile(dataFileAnswer.getFileId());

				if(file.isClosed()) {
					file.open();
				}
				
				file.write(dataFileAnswer.getDataBlock());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public List<DataFileRequest> createRequest() {
		List<DataFileRequest> dataRequest = new ArrayList<>();
		
		try {
			Priority totalPriority = totalPriotity();

			for (LocalFileWriteable file : files) {
				if (file.isOpened() && !file.isCompleted()) {
					float percent = file.getPriority().percent(totalPriority);
					int maxAllowed = (int) percent * downloadLimit.getLimint();

					if (maxAllowed > 0) {
						DataBlockInfo dataBlockInfo = file.createRequest(maxAllowed);
						dataRequest.add(new DataFileRequest(file.getFileId(), dataBlockInfo));						
					}
				}
			}	
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return dataRequest.isEmpty() ? null : dataRequest;
	}
	
	public List<DataFileRequest> createRequest(List<DataFileAnswer> answers) {
		
		return null;
	}

	private Priority totalPriotity() {
		int totalPriority = 0;

		for (LocalFileWriteable localFileWritable : files) {
			totalPriority += localFileWritable.getPriority().get();
		}

		return new Priority(totalPriority);
	}

	public boolean addAll(List<RemoteFileRegistered> list) {
		boolean added = false;
		List<LocalFileWriteable> newFiles = new ArrayList<>();

		for (RemoteFileRegistered remoteFileRegistered : list) {
			added |= newFiles.add(localFileWriteableFactory.create(remoteFileRegistered));
		}

		files.addAll(newFiles);
		return added;
	}

	public boolean removeAll(List<FileId> list) {
		return !files.removeAll(list).isEmpty();
	}
}
