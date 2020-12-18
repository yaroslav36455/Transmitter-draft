package ua.itea.gui;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Window;

public class GUIConnectionController implements Initializable {
	@FXML private Button addFiles;
	@FXML private Button removeFiles;
	@FXML private Button createConnection;
	@FXML private SplitPane splitPane;
	@FXML private BorderPane leftBorderPane;
	@FXML private BorderPane rightBorderPane;
	@FXML private TableView<GUILocalFileRow> localComputer;
	@FXML private TableView<GUILocalFileRow> remoteComputer;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		splitPane.setDividerPositions(0.5);

		localComputer.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		remoteComputer.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

		GUIFileChooserWrapper fileChooser = new GUIFileChooserWrapperCached();
		addFiles.setOnAction(event -> {
			Window window = addFiles.getScene().getWindow();
			List<File> files = fileChooser.getFiles(window);
			
			if (files != null) {
				for (File file : files) {
					String fileName = file.getName();
					String filePath = file.getPath();
					String fileSize = "***";
					String totalFileSize = "8888";
					ProgressBar progressBar = new ProgressBar();
					
					GUILocalFileRow lfr = new GUILocalFileRow(fileName, filePath, fileSize, totalFileSize, progressBar);
					localComputer.getItems().add(lfr);
				}
			}
		});

		removeFiles.setOnAction(event -> {
			localComputer.getItems().removeAll(localComputer.getSelectionModel().getSelectedItems());
		});
	}
}
