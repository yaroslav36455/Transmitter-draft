package ua.itea.gui;

import java.io.File;
import java.io.IOException;

import javafx.geometry.NodeOrientation;
import javafx.scene.control.ProgressBar;
import ua.itea.gui.modellink.MemorySizeUpdateable;
import ua.itea.model.FileId;
import ua.itea.model.LocalFileWriteable;
import ua.itea.model.MemorySize;

public class GUIFileWriteableTreeTableRow extends LocalFileWriteable
										  implements GUITreeTableRow {
	private static final long serialVersionUID = 5655194346337609329L;
	private ProgressBar progressBar;
	private String remoteFileName;
	
	public GUIFileWriteableTreeTableRow(FileId fileId, File file,
										MemorySize remoteFileSize) {
		super(fileId, file);
		super.setTotalSize(remoteFileSize);
		remoteFileName = file.getName();
		progressBar = new ProgressBar();
		progressBar.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
	}
	
	@Override
	public void open() throws IOException {
		setLocalFileSize(new MemorySizeUpdateable());
		super.open();
	}
	
	public String getLocalFileName() {
		return super.isOpened() ? super.getName() : null;
	}
	
	public String getLocalFilePath() {
		return super.isOpened() ? super.getPath() : null;
	}
	
	public MemorySize getLocalFileSize() {
		return super.getFilledSize();
	}
	
	public void setLocalFileSize(MemorySizeUpdateable localFileSize) {
		super.setFilledSize(localFileSize);
		
		localFileSize.setUpdateCallback(()->{
			progressBar.setProgress((double) localFileSize.getSize()
				  				    / (double) getRemoteFileSize().getSize());
		});
		
		localFileSize.update();
	}
	
	public ProgressBar getProgressBar() {
		return getLocalFileSize() == null ? null : progressBar;
	}
	
	public String getRemoteFileName() {
		return remoteFileName;
	}
	
	public MemorySize getRemoteFileSize() {
		return super.getTotalSize();
	}
}
