package ua.itea.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class LocalFileReadable extends LocalFile {
	private RandomAccessFile raf;
	
	public LocalFileReadable(File file) throws FileNotFoundException {
		super(new FileId(), file);
		raf = new RandomAccessFile(file, "r");
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
	public FileSize getFileSize() throws IOException {
//		return new FileSize(raf.getFilePointer(), raf.length());
		return new FileSize(raf.length(), raf.length());
	}
}
