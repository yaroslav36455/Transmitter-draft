package ua.itea.model;

import java.io.Serializable;

//заменить RegisterFileRequest на FileStateChanged - сообщение, что информирует об изменении
//состояние remote файла, его размер, регистрации и прочее
public class FileStateChangedRequest implements Serializable {
	private static final long serialVersionUID = -513527906654672913L;
	private final RemoteFile remoteFile;
	
	public FileStateChangedRequest(RemoteFile remoteFile) {
		this.remoteFile = remoteFile;
	}
	
	public RemoteFile getRemoteFile() {
		return remoteFile;
	}
}
