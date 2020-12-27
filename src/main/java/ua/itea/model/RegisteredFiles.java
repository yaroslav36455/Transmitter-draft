package ua.itea.model;

public class RegisteredFiles {
	private FileBase<RemoteFile> files;
	
	public RegisteredFiles(FileBase<RemoteFile> files) {
		this.files = files;
	}

	public FileBase<RemoteFile> getFiles() {
		return files;
	}

	public void setFiles(FileBase<RemoteFile> files) {
		this.files = files;
	}
}
