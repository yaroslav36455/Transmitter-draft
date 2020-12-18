package ua.itea.model;

public class LocalFileBase {
	private FileBase<LocalFileReadable> readableBase;
	private FileBase<LocalFileWriteable> writeableBase;
	
	public boolean add(LocalFileReadable readableFile) {
		return false;
	}

	public FileBase<LocalFileReadable> getReadableBase() {
		return readableBase;
	}

	public void setReadableBase(FileBase<LocalFileReadable> readableBase) {
		this.readableBase = readableBase;
	}

	public FileBase<LocalFileWriteable> getWriteableBase() {
		return writeableBase;
	}

	public void setWriteableBase(FileBase<LocalFileWriteable> writeableBase) {
		this.writeableBase = writeableBase;
	}
}
