package ua.itea.gui;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Window;
import ua.itea.db.Contact;
import ua.itea.gui.factory.GUIConnectionInfoFactory;
import ua.itea.gui.factory.GUIContactDatabaseDialogFactory;
import ua.itea.gui.modellink.ClientGUIChannelSocketFactory;
import ua.itea.model.Channel;
import ua.itea.model.ClientChannelFactory;
import ua.itea.model.ConnectionClient;
import ua.itea.model.Downloader;
import ua.itea.model.FileBase;
import ua.itea.model.FileSize;
import ua.itea.model.LocalFileReadable;
import ua.itea.model.Mark;
import ua.itea.model.ServerChannelFactory;
import ua.itea.model.Uploader;

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
	private Button addRemoteFiles;
	@FXML
	private Button removeFiles;
	@FXML
	private TableView<GUILocalFileRow> localComputer;
	@FXML
	private TableView<GUIRemoteFileRow> remoteComputer;

	private GUIConnectionInfo connectionInfo;
	private GUIContactDatabaseDialog contactDatabaseDialog;

	private ClientChannelFactory ccf;
	private ServerChannelFactory scf;

	private Channel channel;
	private Downloader downloader;
	private Uploader uploader;

	private ClientGUIChannelSocketFactory cgcsf;
//	private Client client;

	public GUIChannelController() throws IOException {
		uploader = new Uploader(new FileBase<>());
		downloader = new Downloader(new FileBase<>());

		GUIContactDatabaseDialogFactory gcddf = new GUIContactDatabaseDialogFactory();
		contactDatabaseDialog = gcddf.create();

		GUIConnectionInfoFactory gcif = new GUIConnectionInfoFactory();
		connectionInfo = gcif.create();

//		cgcsf = new ClientGUIChannelSocketFactory(connectionInfo.getController());
//		ChannelProvider clientChannelProvider = new ChannelProvider(new ChannelBase(), new ClientChannelFactory());
//		client = new Client(clientChannelProvider, new GUIClientSocketFactory());
	}

	public Text getName() {
		return connectionInfo.getController().getName();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		localComputer.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		remoteComputer.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

		GUIFileChooserWrapper fileChooser = new GUIFileChooserWrapperCached();
		addLocalFiles.setOnAction(event -> {
			try {
				Window window = addLocalFiles.getScene().getWindow();
				List<File> files = fileChooser.getFiles(window);

				if (files != null) {
					for (File file : files) {
						FileBase<LocalFileReadable> fileBase = uploader.getFiles();
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
								} else if (mark == Mark.REJECT) {
									gocd.setReject();
								}
							});

						} catch (ClassNotFoundException | IOException e) {
							e.printStackTrace();
						} finally {
							try {
								c.close();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}).start();
				});
			} catch (IOException e) {
				e.printStackTrace();
			}
		});

		selectButton.setOnAction(event -> {
			Optional<Contact> result = contactDatabaseDialog.showAndWait();

			if (result.isPresent()) {
				Contact contact = result.get();
				addressTextField.setText(contact.getAddress());
				portTextField.setText(String.valueOf(contact.getPort()));
			}
		});
	}
}
