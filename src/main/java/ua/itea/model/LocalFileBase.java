package ua.itea.model;

public class LocalFileBase {
	private FileBase<LocalFileReadable> readableBase;
	private FileBase<LocalFileWriteable> writableBase;
	
	public boolean add(LocalFileReadable readableFile) {
		return false;
	}
	
	public FileBase<LocalFileReadable> getReadableBase() {
		return readableBase;
	}
	
	public void setReadableBase(FileBase<LocalFileReadable> readableBase) {
		this.readableBase = readableBase;
	}
	
	public FileBase<LocalFileWriteable> getWritableBase() {
		return writableBase;
	}
	
	public void setWritableBase(FileBase<LocalFileWriteable> writableBase) {
		this.writableBase = writableBase;
	}
}
