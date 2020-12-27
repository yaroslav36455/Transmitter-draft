package ua.itea.gui;

import javafx.scene.control.ProgressBar;
import ua.itea.model.FileSize;
import ua.itea.model.MemorySize;

public class GUIRemoteFileRow {
	private String fileName;
	private FileSize fileSize;
	private ProgressBar progressBar;

	public GUIRemoteFileRow(String fileName, FileSize fileSize, ProgressBar progressBar) {
		this.fileName = fileName;
		this.fileSize = fileSize;
		this.progressBar = progressBar;
	}

	public String getFileName() {
		return fileName;
	}

	public MemorySize getFilledSize() {
		return fileSize.getFilledSize();
	}

	public MemorySize getTotalFileSize() {
		return fileSize.getTotalSize();
	}

	public ProgressBar getProgressBar() {
		return progressBar;
	}

}
