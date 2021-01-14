package ua.itea.model;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class FileBase<T extends LocalFile> implements Iterable<T>,
													  Serializable {
	private static final long serialVersionUID = 8767955375425168764L;
	private Set<T> files;
	
	public FileBase() {
		files = new HashSet<>();
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
	
	public List<T> addAll(List<T> newFiles) {
		List<T> added = new ArrayList<>();
		
		for(T file : newFiles) {
			if (files.add(file)) {
				added.add(file);
			}
		}
		
		return added;
	}
	
	private T remove(FileId fileId) {
		Iterator<T> iterator = files.iterator();
		T removed = null;
		
		try {
			while(iterator.hasNext() || removed == null) {
				T file = iterator.next();
				
				if(file.getFileId().get() == fileId.get()) {
					iterator.remove();
					removed = file;
					
					if(file.isOpened()) {
						file.close();
					}
				}
			}	
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return removed;
	}
	
	public List<T> removeAll(List<FileId> idList) {
		List<T> removed = new ArrayList<>();
		
		for (FileId fileId : idList) {
			T file = remove(fileId);
					
			if(file != null) {
				removed.add(file);
			}
		}
		
		return removed;
	}
	
	public void clear() {
		for (T f : files) {
			if (f.isOpened()) {
				try {
					f.close();	
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		files.clear();
	}
	
	public int size() {
		return files.size();
	}
	
	public boolean isEmpty() {
		return files.isEmpty();
	}
	
	public boolean contains(FileId fileId) {
		for (T f : files) {
			if(f.getFileId().get() == fileId.get()) {
				return true;
			}
		}
		
		return false;
	}
}
