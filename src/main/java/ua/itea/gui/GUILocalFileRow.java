package ua.itea.gui;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ProgressBar;

public class GUILocalFileRow implements Initializable {
	@FXML private String fileName;
	@FXML private String filePath;
	
	private long timeElapsed;
	private long timeLeft;
	
	@FXML private String fileSize;
	@FXML private String totalFileSize;
	
	@FXML private ProgressBar progressBar;
	
	public GUILocalFileRow() {
		/* empty */
	}

	public GUILocalFileRow(String fileName, String filePath,
						String fileSize, String totalFileSize,
			ProgressBar progressBar) {
		this.fileName = fileName;
		this.filePath = filePath;
		this.fileSize = fileSize;
		this.totalFileSize = totalFileSize;
		this.progressBar = progressBar;
	}
	
	

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getFileSize() {
		return fileSize;
	}

	public void setFileSize(String fileSize) {
		this.fileSize = fileSize;
	}

	public String getTotalFileSize() {
		return totalFileSize;
	}

	public void setTotalFileSize(String totalFileSize) {
		this.totalFileSize = totalFileSize;
	}

	public ProgressBar getProgressBar() {
		return progressBar;
	}

	public void setProgressBar(ProgressBar progressBar) {
		this.progressBar = progressBar;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		
	}
}
