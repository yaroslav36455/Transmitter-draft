package ua.itea.gui;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableView;
import javafx.scene.text.Text;
import javafx.stage.Window;
import ua.itea.config.Config;
import ua.itea.db.Contact;
import ua.itea.gui.factory.GUIConnectionInfoFactory;
import ua.itea.gui.factory.GUIContactDatabaseDialogFactory;
import ua.itea.gui.factory.GUIFileReadableTreeTableRowFactory;
import ua.itea.gui.factory.GUIFileWriteableTreeTableRowFactory;
import ua.itea.gui.modellink.GUIDownloaderFiles;
import ua.itea.gui.modellink.GUIUploaderFiles;
import ua.itea.model.Channel;
import ua.itea.model.Connection;
import ua.itea.model.ConnectionClient;
import ua.itea.model.Downloader;
import ua.itea.model.FileId;
import ua.itea.model.Loader;
import ua.itea.model.Mark;
import ua.itea.model.Messenger;
import ua.itea.model.Priority;
import ua.itea.model.Uploader;
import ua.itea.model.message.factory.DataMessageFactory;

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
	private Button addLocalFiles;
	@FXML
	private Button removeFiles;
	@FXML
	private Button downloadFiles;
	@FXML
	private TreeTableView<GUITreeTableRow> treeTable;

	private GUIConnectionInfo connectionInfo;
	private GUIContactDatabaseDialog contactDatabaseDialog;

	private Channel channel;
	private Config config;

	public GUIChannelController() throws IOException {
		config = new Config();

		GUIContactDatabaseDialogFactory gcddf = new GUIContactDatabaseDialogFactory();
		contactDatabaseDialog = gcddf.create();

		GUIConnectionInfoFactory gcif = new GUIConnectionInfoFactory();
		connectionInfo = gcif.create();
	}

	public Text getName() {
		return connectionInfo.getController().getName();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		TreeItem<GUITreeTableRow> itemRoot = new TreeItem<>(new GUIEmptyTreeTableRow());
		treeTable.setShowRoot(false);
		treeTable.setRoot(itemRoot);

		channel = createChannel(treeTable);

		treeTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

		GUIFileChooserWrapper fileChooser = new GUIFileChooserWrapperCached();
		addLocalFiles.setOnAction(event -> {
			Window window = addLocalFiles.getScene().getWindow();

			List<File> files = fileChooser.getFiles(window);
			if (files != null) {
				channel.registerFiles(files);
			}
		});

		connectionInfo.getController().getAddress().textProperty().bind(addressTextField.textProperty());
		connectionInfo.getController().getPort().textProperty().bind(portTextField.textProperty());
		connectButton.setOnAction(event -> {
			String host = connectionInfo.getController().getAddress().getText();
			int port = Integer.valueOf(connectionInfo.getController().getPort().getText());

			try {
				Socket socket = new Socket(InetAddress.getByName(host), port);
				ConnectionClient c = new ConnectionClient(socket);

				GUIConnectionInfo gci = connectionInfo;
				Platform.runLater(() -> {
					GUIOutgoingConnectionDialog gocd = new GUIOutgoingConnectionDialog(gci);
					gocd.show();

					new Thread(() -> {
						try {
							c.start();
							c.writeName(gci.getController().getName().getText());

							Mark mark = c.readMark();

							Platform.runLater(() -> {
								if (mark == Mark.ACCEPT) {
									gocd.setAccept();
									start(c);
								} else { // mark == Mark.REJECT
									gocd.setReject();
									try {
										c.close();
									} catch (IOException ex) {
										ex.printStackTrace();
									}
								}
							});

						} catch (ClassNotFoundException | IOException e) {
							e.printStackTrace();
							try {
								c.close();
							} catch (IOException ex) {
								ex.printStackTrace();
							}
						}
					}).start();
				});
			} catch (IOException e) {
				e.printStackTrace();
			}
		});

		removeFiles.setOnAction(event -> {
//			ObservableList<GUILocalFileRow> selected = localComputer.getSelectionModel().getSelectedItems();
//			
//			Uploader.Registered ur = channel.getLoader().getUploader().getRegistered();
//			Downloader.Registered dr = channel.getLoader().getDownloader().getRegistered();
//			
//			for (GUILocalFileRow guiLocalFileRow : selected) {	
//				try {
//					ur.remove
//					ur.removeCached(guiLocalFileRow.getFileHandler().getFileId());
//					dr.remove
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//			}
//			
//			localComputer.getItems().removeAll(selected);
		});

		selectButton.setOnAction(event -> {
			Optional<Contact> result = contactDatabaseDialog.showAndWait();

			if (result.isPresent()) {
				Contact contact = result.get();
				addressTextField.setText(contact.getAddress());
				portTextField.setText(String.valueOf(contact.getPort()));
				connectionInfo.getController().getName().setText(contact.getName());
			}
		});

		downloadFiles.setOnAction(event -> {
			ObservableList<TreeItem<GUITreeTableRow>> rows = treeTable.getSelectionModel().getSelectedItems();
			List<FileId> idList = new ArrayList<>();
			
			for (TreeItem<GUITreeTableRow> treeItem : rows) {
				idList.add(treeItem.getValue().getFileId());
			}
			
			try {
				channel.beginDownloading(idList);
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
	}

	private Channel createChannel(TreeTableView<GUITreeTableRow> treeTable) {
		Channel channel = new Channel();
		Loader loader = new Loader();
		Uploader uploader = new Uploader();
		Downloader downloader = new Downloader();

		uploader.setRegistered(uploader.new Registered());
		uploader.setLoadLimit(config.getUploadLimit());
		uploader.setFiles(new GUIUploaderFiles(treeTable));
		uploader.setLocalFileReadableFactory(new GUIFileReadableTreeTableRowFactory());

		downloader.setLoadLimit(config.getDownloadLimit());
		downloader.setFiles(new GUIDownloaderFiles(treeTable));
		downloader.setLocalFileWriteableFactory(
				new GUIFileWriteableTreeTableRowFactory(config.getDownloadDirectory(), new Priority(1)));

		loader.setDownloader(downloader);
		loader.setUploader(uploader);
		loader.setMessageFactory(new DataMessageFactory());

		channel.setLoader(loader);
		channel.setMessenger(new Messenger());

		return channel;
	}

	public void start(Connection c) {
		channel.getMessenger().setConnection(c);
		channel.start();
	}

	public void close() {
		if (channel.isRunning()) {
			channel.stop();
		}
	}
}
