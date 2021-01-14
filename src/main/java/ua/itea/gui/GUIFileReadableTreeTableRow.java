package ua.itea.gui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javafx.scene.control.ProgressBar;
import ua.itea.gui.modellink.MemorySizeUpdateable;
import ua.itea.model.DataBlock;
import ua.itea.model.DataBlockInfo;
import ua.itea.model.FileId;
import ua.itea.model.LocalFileReadable;
import ua.itea.model.MemorySize;

public class GUIFileReadableTreeTableRow extends LocalFileReadable
										 implements GUITreeTableRow {
	private static final long serialVersionUID = 3872688830488392170L;
	private MemorySize localFileSize;
	
	private ProgressBar progressBar;
	
	private String remoteFileName;
	private MemorySizeUpdateable remoteFileSize;
	
	public GUIFileReadableTreeTableRow(FileId fileId, File file) {
		super(fileId, file);
		progressBar = new ProgressBar();
		setLocalFileSize(new MemorySize(file.length()));
	}
	
	@Override
	public void open() throws FileNotFoundException {
		super.open();
		setRemoteFileName(getLocalFileName());
		setRemoteFileSize(new MemorySizeUpdateable(0));
	}
	
	@Override
	public DataBlock read(DataBlockInfo dataBlockInfo) throws IOException {
		DataBlock dataBlock = super.read(dataBlockInfo);
		
		remoteFileSize.setSize(remoteFileSize.getSize() + dataBlock.getSize());
		return dataBlock;
	}

	@Override
	public String getLocalFileName() {
		return super.getName();
	}
	
	@Override
	public String getLocalFilePath() {
		return super.getPath();
	}
	
	@Override
	public MemorySize getLocalFileSize() {
		return localFileSize;
	}
	
	public void setLocalFileSize(MemorySize localFileSize) {
		this.localFileSize = localFileSize;
		if(remoteFileSize != null) {
			remoteFileSize.update();
		}
	}
	
	@Override
	public ProgressBar getProgressBar() {
		return getLocalFileSize() == null || remoteFileName == null ? null
															        : progressBar;
	}
	
	@Override
	public String getRemoteFileName() {
		return remoteFileName;
	}
	
	public void setRemoteFileName(String remoteFileName) {
		this.remoteFileName = remoteFileName;
	}
	
	@Override
	public MemorySize getRemoteFileSize() {
		return remoteFileSize;
	}
	
	public void setRemoteFileSize(MemorySizeUpdateable remoteFileSize) {
		this.remoteFileSize = remoteFileSize;
		
		this.remoteFileSize.setUpdateCallback(()->{
			if (getLocalFileSize() != null)
			progressBar.setProgress((double) this.remoteFileSize.getSize()
								  	/ (double) getLocalFileSize().getSize());
		});
		
		this.remoteFileSize.update();
	}
}
