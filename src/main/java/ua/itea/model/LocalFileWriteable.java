package ua.itea.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class LocalFileWriteable extends LocalFile {
	private RandomAccessFile raf;
	private Priority priority;
	
	// file, fileId, totalSize, priority
	public LocalFileWriteable(WriteableFileInfo fileInfo) throws IOException {
		super(fileInfo.getFileId(), fileInfo.getFile());
		raf = new RandomAccessFile(fileInfo.getFile(), "rw");
		priority = fileInfo.getPriority();
		raf.setLength(fileInfo.getTotalSize().getSize());
		
		System.out.println("pointer " + raf.getFilePointer());
		System.out.println("length " + raf.length());
	}

	public void write(DataBlock dataBlock) throws IOException {
		long pointer = dataBlock.getOffset();
		
		if (pointer != raf.getFilePointer()) {
			raf.seek(pointer);
		}
		
		raf.write(dataBlock.getData());
	}

	@Override
	public FileSize getFileSize() throws IOException {
		return new FileSize(raf.getFilePointer(), raf.length());
	}
	
	public Priority getPriority() {
		return priority;
	}

	public void setPriority(Priority priority) {
		this.priority = priority;
	}
	
	public DataBlockInfo createRequest(int maxAllowed) throws IOException {
		final long offset = raf.getFilePointer();
		final int size = (int) Math.min(raf.length() - offset, maxAllowed);
		
		return new DataBlockInfo(offset, size);	
	}
	
	public boolean isCompleted() throws IOException {
		return raf.getFilePointer() == raf.length();
	}
}
