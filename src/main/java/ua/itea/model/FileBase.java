package ua.itea.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class FileBase<T extends FileHandler> implements Iterable<T> {
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
		files.add(localFile);
		return true;
	}
}
