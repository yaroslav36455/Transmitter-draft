package ua.itea.gui;

import javafx.scene.control.ProgressBar;
import ua.itea.model.FileId;
import ua.itea.model.MemorySize;

public interface GUITreeTableRow {
	
	public FileId getFileId();
	
	public String getLocalFileName();
	
	public String getLocalFilePath();
	
	public MemorySize getLocalFileSize();
	
	public ProgressBar getProgressBar();
	
	public String getRemoteFileName();
	
	public MemorySize getRemoteFileSize();
}
