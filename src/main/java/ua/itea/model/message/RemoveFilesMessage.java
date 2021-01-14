package ua.itea.model.message;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import ua.itea.model.FileId;

public class RemoveFilesMessage implements LoaderMessage, Serializable,
										   Iterable<FileId> {
	private static final long serialVersionUID = 8460909561097628165L;
	private List<FileId> idList;
	
	
	public RemoveFilesMessage() {
		idList = new ArrayList<>();
	}
	
	public boolean isEmpty() {
		return idList.isEmpty();
	}
	
	public void add(FileId fileId) {
		idList.add(fileId);
	}

	public Iterator<FileId> iterator() {
		return idList.iterator();
	}
	
	public int size() {
		return idList.size();
	}
	
	public List<FileId> getList() {
		return idList;
	}
}
