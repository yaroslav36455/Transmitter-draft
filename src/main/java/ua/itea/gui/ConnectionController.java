package ua.itea.gui;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Window;

public class ConnectionController implements Initializable {
	@FXML private Button addFiles;
	@FXML private Button removeFiles;
	@FXML private SplitPane splitPane;
	@FXML private BorderPane leftBorderPane;
	@FXML private BorderPane rightBorderPane;
	@FXML private TableView<LocalFileRow> localComputer;
	@FXML private TableView<LocalFileRow> remoteComputer;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		splitPane.setDividerPositions(0.5);

//		setTable(localComputer);
//		setTable(remoteComputer);

		leftBorderPane.setCenter(localComputer);
		rightBorderPane.setCenter(remoteComputer);

		FileChooserGUI fileProvider = new FileChooserGUICached();
		addFiles.setOnAction(event -> {
			Window window = addFiles.getScene().getWindow();
			List<File> files = fileProvider.getFiles(window);
			
			if (files != null) {
				for (File file : files) {
					System.out.println(file.canRead());
					System.out.println(file.getName() + " | " + file.getParentFile());
					
					String fileName = file.getName();
					String filePath = file.getPath();
					String fileSize = "";
					String totalFileSize = "";
					ProgressBar progressBar = new ProgressBar();
					
					LocalFileRow lfr = new LocalFileRow(fileName, filePath, fileSize, totalFileSize, progressBar);
					localComputer.getItems().add(lfr);
				}
			}
		});

		removeFiles.setOnAction(event -> {
			localComputer.getItems().removeAll(localComputer.getSelectionModel().getSelectedItems());
		});
	}

	private static void setTable(TableView<LocalFileRow> table) {
		ObservableList<LocalFileRow> localFiles = FXCollections.observableArrayList(

				new LocalFileRow("cat.png", "/home/vessel/Documents", "5.4 GiB", "32.23 GiB", new ProgressBar(0.24)),
				new LocalFileRow("document.doc", "/home/vessel/Documents", "1 MiB", "2 MiB", new ProgressBar(0.95)),
				new LocalFileRow("simple.png", "/usr/lib", "140", "270", new ProgressBar(0.55)),
				new LocalFileRow("true", "/bin", "22 MiB", "66 MiB", new ProgressBar(0.67)));

		table.setItems(localFiles);

		TableColumn<LocalFileRow, String> nameColumn = new TableColumn<>("Name");
		TableColumn<LocalFileRow, String> pathColumn = new TableColumn<>("Path");
		TableColumn<LocalFileRow, String> sizeColumn = new TableColumn<>("Size");
		TableColumn<LocalFileRow, String> totalSizeColumn = new TableColumn<>("Total");
		TableColumn<LocalFileRow, ProgressBar> progressColumn = new TableColumn<>("Progress");

		nameColumn.setCellValueFactory(new PropertyValueFactory<>("fileName"));
		pathColumn.setCellValueFactory(new PropertyValueFactory<>("filePath"));
		sizeColumn.setCellValueFactory(new PropertyValueFactory<>("fileSize"));
		totalSizeColumn.setCellValueFactory(new PropertyValueFactory<>("totalFileSize"));
		progressColumn.setCellValueFactory(new PropertyValueFactory<>("progressBar"));

		table.getColumns().add(nameColumn);
		table.getColumns().add(pathColumn);
		table.getColumns().add(sizeColumn);
		table.getColumns().add(totalSizeColumn);
		table.getColumns().add(progressColumn);

//		table.setOnMouseClicked(e->localFiles.add(new LocalFileRow("file", "/usr/share", "77 MiB", "80 MiB", new ProgressBar(0.97))));

		table.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
	}
}
