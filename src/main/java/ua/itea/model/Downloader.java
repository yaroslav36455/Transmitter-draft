package ua.itea.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ua.itea.model.factory.LocalFileWriteableFactory;
import ua.itea.model.message.DataAnswer;
import ua.itea.model.message.DataRequest;

public class Downloader {
	private LocalFileWriteableFactory localFileWriteableFactory;
	private FileBase<LocalFileWriteable> files;
	private LoadLimit loadLimit;

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
		return loadLimit;
	}

	public void setLoadLimit(LoadLimit downloadLimit) {
		this.loadLimit = downloadLimit;
	}

	public void save(DataAnswer dataAnswer) {
		try {
			for (DataFileAnswer dataFileAnswer : dataAnswer) {
				LocalFileWriteable file = files.getFile(dataFileAnswer.getFileId());

				if (file.isOpened()) {
					file.write(dataFileAnswer.getDataBlock());
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public DataRequest createRequest() {
		DataRequest dataRequest = new DataRequest();
		
		try {
			Priority totalPriority = totalPriotity();

			for (LocalFileWriteable file : files) {
				if (file.isOpened() && !file.isCompleted()) {
					float percent = file.getPriority().percent(totalPriority);
					int maxAllowed = (int) percent * loadLimit.getLimint();

					DataBlockInfo dataBlockInfo = file.createRequest(maxAllowed);

					dataRequest.add(new DataFileRequest(file.getFileId(), dataBlockInfo));
				}
			}	
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return dataRequest.isEmpty() ? null : dataRequest;
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
