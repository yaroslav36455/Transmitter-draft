package ua.itea.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class LocalFileReadable extends LocalFile {
	private static final long serialVersionUID = 1711073754387401146L;
	private RandomAccessFile raf;
	
	public LocalFileReadable(FileId fileId, File file) {
		super(fileId, file);
	}
	
	public void open() throws FileNotFoundException {
		raf = new RandomAccessFile(getFile(), "r");
	}
	
	@Override
	public boolean isOpened() {
		return raf != null; 
	}
	
	@Override
	public boolean isClosed() {
		return raf == null;
	}
	
	public DataBlock read(DataBlockInfo dataBlockInfo) throws IOException {
		long offset = dataBlockInfo.getOffset();
		int size = blockSize(dataBlockInfo);
		byte[] buffer = new byte[size];
		
		if (offset != raf.getFilePointer()) {
			raf.seek(offset);
		}
		raf.read(buffer);
		
		return new DataBlock(offset, buffer);
	}
	
	private int blockSize(DataBlockInfo dataBlockInfo) throws IOException {
		long left = raf.length() - dataBlockInfo.getOffset();
		int sizeAllowed = (int) Math.min(left, Integer.MAX_VALUE);
		
		return Math.min(dataBlockInfo.getSize(), sizeAllowed);
	}

	@Override
	public void close() throws IOException {
		raf.close();
		raf = null;
	}
}
