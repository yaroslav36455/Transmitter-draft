package ua.itea.model;

import java.io.Serializable;
import java.util.Objects;

public abstract class FileHandler implements Serializable {
	private static final long serialVersionUID = -8540875129603292780L;
	private final FileId fileId;
	
	public FileHandler(FileId fileId) {
		this.fileId = fileId;
	}

	public FileId getFileId() {
		return fileId;
	}

	@Override
	public int hashCode() {
		return Objects.hash(fileId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		FileHandler other = (FileHandler) obj;
		return Objects.equals(fileId, other.fileId);
	}
}
