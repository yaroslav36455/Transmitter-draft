package ua.itea.model;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public abstract class LocalFile extends FileHandler implements AutoCloseable {
	private static final long serialVersionUID = -727464069161641614L;
	private final File file;
	
	public LocalFile(FileId fileId, File file) {
		super(fileId);
		this.file = file;
	}
	
	public String getName() {
		return file.getName();
	}
	
	public String getPath() {
		return file.getParent();
	}

	public File getFile() {
		return file;
	}
	
	@Override
	public abstract void close() throws IOException;
	
	public boolean isOpened() {
		return !isClosed();
	}
	
	public abstract boolean isClosed();

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(file);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!super.equals(obj)) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		LocalFile other = (LocalFile) obj;
		return Objects.equals(file, other.file);
	}
}
