package ua.itea.gui;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.stage.Window;
import ua.itea.db.Connector;
import ua.itea.db.ConnectorImpl;
import ua.itea.db.Contact;
import ua.itea.db.ContactDatabase;
import ua.itea.gui.factory.GUIConnectionInfoFactory;
import ua.itea.gui.factory.GUIConnectionInfoImplFactory;
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
	private Button selectButton;
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
	private TableView<GUIRemoteFileRow> remoteComputer;
	private Channel channel;
	private GUIConnectionInfo connectionInfo;

	public GUIChannelController() throws IOException {
		LocalFileBase localFileBase = new LocalFileBase();

		localFileBase.setReadableBase(new FileBase<>());

		channel = new Channel();
		channel.setLocalFileBase(localFileBase);

		GUIConnectionInfoFactory gcif = new GUIConnectionInfoImplFactory();
		connectionInfo = gcif.create();
	}

	public Text getName() {
		return connectionInfo.getController().getName();
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
						double progress = fs.getFilledSize().getSize() / (double) fs.getTotalSize().getSize();
						ProgressBar progressBar = new ProgressBar(progress);

						GUILocalFileRow lfr = new GUILocalFileRow(fileName, filePath, fs, progressBar);
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
		
		connectButton.setOnAction(event -> {
//			connectionInfo.getController().getAddress().setText(addressTextField.getText());
//			connectionInfo.getController().getPort().setText(portTextField.getText());
//
//			Alert alert = new Alert(AlertType.NONE, null, ButtonType.CANCEL);
//
//			alert.getDialogPane().setContent(connectionInfo.getNode());
//			alert.setHeaderText("Connection...");
//			alert.setTitle("Connection");
//			alert.setGraphic(new ProgressIndicator());
//			alert.showAndWait();
			
			try {
				Connector connector = new ConnectorImpl();
				ContactDatabase contactDatabase = new ContactDatabase(connector.getConnection());
				
				contactDatabase.insert(connector.getConnection(),
									 new Contact("QWS ", "127.0.0.2", 999));
			} catch (SQLException e) {
				e.printStackTrace();
			}
		});

		FXMLLoader loader = new FXMLLoader();
		GUIContactDatabaseDialog dialog = new GUIContactDatabaseDialog();

		loader.setController(dialog);
		loader.setLocation(this.getClass().getClassLoader().getResource("contact-database.fxml"));
		try {
			dialog.getDialogPane().setContent(loader.load());
		} catch (IOException e) {
			e.printStackTrace();
		}

		selectButton.setOnAction(event -> {
			Optional<Contact> result = dialog.showAndWait();

			if (result.isPresent()) {
				Contact contact = result.get();
				addressTextField.setText(contact.getAddress());
				portTextField.setText(String.valueOf(contact.getPort()));
			}
		});
	}
}
