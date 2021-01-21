package ua.itea.model.message;

import java.io.Serializable;
import java.util.List;

import ua.itea.model.Channel;
import ua.itea.model.RemoteFileRegistered;

public class NewFilesMessage extends LoaderMessage implements Serializable {
	private static final long serialVersionUID = 133901629814675679L;
	private List<RemoteFileRegistered> registeredFiles;
	
	public NewFilesMessage() {
		/* empty */
	}
	
	public NewFilesMessage(List<RemoteFileRegistered> registeredFiles) {
		this.registeredFiles = registeredFiles;
	}

	public List<RemoteFileRegistered> getList() {
		return registeredFiles;
	}

	public void setList(List<RemoteFileRegistered> registeredFiles) {
		this.registeredFiles = registeredFiles;
	}

	public boolean isEmpty() {
		return registeredFiles == null || registeredFiles.isEmpty();
	}
	
	public int size() {
		return isEmpty() ? 0 : registeredFiles.size();
	}
	
	@Override
	public void execute(Channel loader) {
		loader.getDownloader().addAll(registeredFiles);
	}

}
