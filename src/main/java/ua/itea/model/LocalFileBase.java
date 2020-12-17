package ua.itea.model;

public class LocalFileBase {
	private FileBase<LocalFileReadable> readableBase;
	private FileBase<LocalFileWritable> writableBase;
	
	public FileBase<LocalFileReadable> getReadableBase() {
		return readableBase;
	}
	public void setReadableBase(FileBase<LocalFileReadable> readableBase) {
		this.readableBase = readableBase;
	}
	public FileBase<LocalFileWritable> getWritableBase() {
		return writableBase;
	}
	public void setWritableBase(FileBase<LocalFileWritable> writableBase) {
		this.writableBase = writableBase;
	}
}
