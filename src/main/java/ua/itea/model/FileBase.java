package ua.itea.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class FileBase<T extends FileHandler> implements Iterable<T>,
														Serializable {
	private static final long serialVersionUID = 8767955375425168764L;
	private List<T> files;
	
	public FileBase() {
		files = new ArrayList<>();
	}

	@Override
	public Iterator<T> iterator() {
		return files.iterator();
	}

	public T getFile(FileId fileId) {
		for (T file : files) {
			if (file.getFileId().get() == fileId.get()) {
				return file;
			}
		}
		return null;
	}
	
	public boolean add(T localFile) {
		return files.add(localFile);
	}
	
	public boolean addAll(FileBase<T> newFiles) {
		boolean result = true;
		for (T t : newFiles) {
			result = result && add(t);
		}
		
		return result;
	}
	
	public void clear() {
		files.clear();
	}
}
