package ua.itea.gui;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import ua.itea.model.FileSize;
import ua.itea.model.MemorySize;

public class GUILocalFileRow {
	private String fileName;
	private String filePath;

	private long timeElapsed;
	private long timeLeft;

	private FileSize fileSize;

	private ProgressBar progressBar;
	private String priority;

	public GUILocalFileRow(String fileName, String filePath,
						   FileSize fileSize, ProgressBar progressBar) {
		this.fileName = fileName;
		this.filePath = filePath;
		this.fileSize = fileSize;
		this.progressBar = progressBar;
	}

	public String getFileName() {
		return fileName;
	}

	public String getFilePath() {
		return filePath;
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

	public String getPriority() {
		return priority;
	}
}
