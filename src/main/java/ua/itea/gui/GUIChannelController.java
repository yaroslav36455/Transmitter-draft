package ua.itea.gui;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Window;
import ua.itea.model.Channel;
import ua.itea.model.FileBase;
import ua.itea.model.FileSize;
import ua.itea.model.LocalFileBase;
import ua.itea.model.LocalFileReadable;

public class GUIChannelController implements Initializable {
	@FXML
	private TextField addressTextField;
	@FXML
	private TextField portTextField;
	@FXML
	private Button connectButton;
	@FXML
	private Button addFiles;
	@FXML
	private Button removeFiles;
	@FXML
	private SplitPane splitPane;
	@FXML
	private BorderPane leftBorderPane;
	@FXML
	private BorderPane rightBorderPane;
	@FXML
	private TableView<GUILocalFileRow> localComputer;
	@FXML
	private TableView<GUILocalFileRow> remoteComputer;
	private Channel channel;

	public GUIChannelController() {
		LocalFileBase localFileBase = new LocalFileBase();

		localFileBase.setReadableBase(new FileBase<>());

		channel = new Channel();
		channel.setLocalFileBase(localFileBase);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		splitPane.setDividerPositions(0.5);

		localComputer.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		remoteComputer.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

		GUIFileChooserWrapper fileChooser = new GUIFileChooserWrapperCached();
		addFiles.setOnAction(event -> {
			try {
				Window window = addFiles.getScene().getWindow();
				List<File> files = fileChooser.getFiles(window);

				if (files != null) {
					for (File file : files) {
						FileBase<LocalFileReadable> fileBase = channel.getLocalFileBase().getReadableBase();
						LocalFileReadable localFileReadable = new LocalFileReadable(file);
						fileBase.add(localFileReadable);
						FileSize fs = localFileReadable.getFileSize();

						String fileName = file.getName();
						String filePath = file.getPath();
						String fileSize = String.valueOf(fs.getFilledSize());
						String totalFileSize = String.valueOf(fs.getTotalSize());
						ProgressBar progressBar = new ProgressBar(fs.getFilledSize() / (double) fs.getTotalSize());

						GUILocalFileRow lfr = new GUILocalFileRow(fileName, filePath, fileSize, totalFileSize,
								progressBar);
						localComputer.getItems().add(lfr);
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		});

		removeFiles.setOnAction(event -> {
			localComputer.getItems().removeAll(localComputer.getSelectionModel().getSelectedItems());
		});
	}
}
