package ua.itea.gui;

import javafx.scene.control.ProgressBar;
import ua.itea.model.FileId;
import ua.itea.model.MemorySize;

public class GUIEmptyTreeTableRow implements GUITreeTableRow {

	@Override
	public FileId getFileId() {
		/* empty */
		return null;
	}
	
	@Override
	public String getLocalFileName() {
		/* empty */
		return null;
	}

	@Override
	public String getLocalFilePath() {
		/* empty */
		return null;
	}

	@Override
	public MemorySize getLocalFileSize() {
		/* empty */
		return null;
	}

	@Override
	public ProgressBar getProgressBar() {
		/* empty */
		return null;
	}

	@Override
	public String getRemoteFileName() {
		/* empty */
		return null;
	}

	@Override
	public MemorySize getRemoteFileSize() {
		/* empty */
		return null;
	}
}
