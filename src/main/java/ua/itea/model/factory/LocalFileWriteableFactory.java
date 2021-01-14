package ua.itea.model.factory;

import java.io.File;

import ua.itea.model.FileId;
import ua.itea.model.LocalFileWriteable;
import ua.itea.model.Priority;
import ua.itea.model.RemoteFileRegistered;

public class LocalFileWriteableFactory {
	private String defaultPath;
	private Priority defaultPriority;
	
	public LocalFileWriteableFactory(String defaultPath, Priority defaultPriority) {
		this.defaultPath = defaultPath;
		this.defaultPriority = defaultPriority;
	}

	public String getDefaultPath() {
		return defaultPath;
	}

	public void setDefaultPath(String defaultPath) {
		this.defaultPath = defaultPath;
	}

	public Priority getDefaultPriority() {
		return defaultPriority;
	}

	public void setDefaultPriority(Priority defaultPriority) {
		this.defaultPriority = defaultPriority;
	}

	public LocalFileWriteable create(RemoteFileRegistered registered) {
		FileId fileId = registered.getFileId();
		File file = new File(defaultPath + registered.getName());
		
		LocalFileWriteable localFile = new LocalFileWriteable(fileId, file);
		
		localFile.setPriority(defaultPriority);
		
		return localFile;
	}
}
