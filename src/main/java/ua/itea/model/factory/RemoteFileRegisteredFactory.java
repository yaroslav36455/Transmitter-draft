package ua.itea.model.factory;

import ua.itea.model.LocalFileReadable;
import ua.itea.model.MemorySize;
import ua.itea.model.RemoteFileRegistered;

public class RemoteFileRegisteredFactory {
	public RemoteFileRegistered create(LocalFileReadable readableFile) {
		return new RemoteFileRegistered(readableFile.getFileId(),
										readableFile.getName(),
										new MemorySize(readableFile.getFile().length()));
	}
}
