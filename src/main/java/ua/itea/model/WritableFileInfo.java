package ua.itea.model;

import java.io.File;

public class WritableFileInfo {
	private FileId fileId;
	private File file;
	private FileTotalSize totalSize;
	private Priority priority;
	
	public WritableFileInfo(FileId fileId, File file, FileTotalSize totalSize, Priority priority) {
		this.fileId = fileId;
		this.file = file;
		this.totalSize = totalSize;
		this.priority = priority;
	}

	public FileId getFileId() {
		return fileId;
	}

	public void setFileId(FileId fileId) {
		this.fileId = fileId;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public FileTotalSize getTotalSize() {
		return totalSize;
	}

	public void setTotalSize(FileTotalSize totalSize) {
		this.totalSize = totalSize;
	}

	public Priority getPriority() {
		return priority;
	}

	public void setPriority(Priority priority) {
		this.priority = priority;
	}
}
