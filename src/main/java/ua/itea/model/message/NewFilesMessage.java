package ua.itea.model.message;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import ua.itea.model.FileBase;
import ua.itea.model.RemoteFileRegistered;

public class NewFilesMessage implements LoaderMessage, Serializable,
										Iterable<RemoteFileRegistered> {
	private static final long serialVersionUID = -344337412506311091L;
	private List<RemoteFileRegistered> registeredFiles;
	
	public NewFilesMessage() {
		registeredFiles = new ArrayList<>();
	}
	
	public boolean isEmpty() {
		return registeredFiles.isEmpty();
	}
	
	public void add(RemoteFileRegistered registered) {
		registeredFiles.add(registered);
	}

	public Iterator<RemoteFileRegistered> iterator() {
		return registeredFiles.iterator();
	}
	
	public int size() {
		return registeredFiles.size();
	}

	public List<RemoteFileRegistered> getList() {
		return registeredFiles;
	}
}
