package ua.itea.model;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class LocalFileWriteable extends LocalFile {
	private static final long serialVersionUID = 5357149757137249326L;
	private RandomAccessFile raf;
	private Priority priority;
	private MemorySize totalSize;
	private MemorySize filledSize;
	
	public LocalFileWriteable(FileId fileId, File file) {
		super(fileId, file);
	}
	
	public MemorySize getFilledSize() {
		return filledSize;
	}

	public void setFilledSize(MemorySize filledSize) {
		this.filledSize = filledSize;
	}

	public void setTotalSize(MemorySize totalSize) {
		this.totalSize = totalSize;
	}

	public Priority getPriority() {
		return priority;
	}

	public void setPriority(Priority priority) {
		this.priority = priority;
	}
	
	public MemorySize getTotalSize() {
		return totalSize;
	}
	
	public void open() throws IOException {
		this.raf = new RandomAccessFile(getFile(), "rw");
		this.raf.setLength(totalSize.getSize());
	}
	
	@Override
	public boolean isClosed() {
		return raf == null;
	}

	public void write(DataBlock dataBlock) throws IOException {
		long pointer = dataBlock.getOffset();
		
		if (pointer != raf.getFilePointer()) {
			raf.seek(pointer);
		}
		
		raf.write(dataBlock.getData());
		filledSize.setSize(pointer);
	}
	
	public DataBlockInfo createRequest(int maxAllowed) throws IOException {
		final long offset = raf.getFilePointer();
		final int size = (int) Math.min(raf.length() - offset, maxAllowed);
		
		return new DataBlockInfo(offset, size);	
	}
	
	public boolean isCompleted() throws IOException {
		return raf.getFilePointer() == raf.length();
	}

	@Override
	public void close() throws IOException {
		raf.close();
		raf = null;
	}
}
