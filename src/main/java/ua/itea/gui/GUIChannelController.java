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
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Window;
import ua.itea.config.Config;
import ua.itea.db.Contact;
import ua.itea.gui.factory.GUIConnectionInfoFactory;
import ua.itea.gui.factory.GUIContactDatabaseDialogFactory;
import ua.itea.gui.modellink.GUIDownloaderFiles;
import ua.itea.gui.modellink.GUIDownloaderRegistered;
import ua.itea.gui.modellink.GUIUploaderFiles;
import ua.itea.model.Channel;
import ua.itea.model.ChannelFactory;
import ua.itea.model.Connection;
import ua.itea.model.ConnectionClient;
import ua.itea.model.Downloader;
import ua.itea.model.FileBase;
import ua.itea.model.FileId;
import ua.itea.model.FileTotalSize;
import ua.itea.model.LocalFileReadable;
import ua.itea.model.LocalFileWriteable;
import ua.itea.model.Mark;
import ua.itea.model.Priority;
import ua.itea.model.RemoteFile;
import ua.itea.model.Uploader;
import ua.itea.model.WriteableFileInfo;

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

	private ChannelFactory channelFactory;

	private Channel channel;
	private Downloader downloader;
	private Uploader uploader;
	
	private Config config;

//	private ClientGUIChannelSocketFactory cgcsf;
//	private Client client;

	public GUIChannelController() throws IOException {
		config = new Config();
		
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
		uploader = new Uploader();
		uploader.setFiles(new GUIUploaderFiles(localComputer));
		uploader.setRegistered(new FileBase<>());
		
		downloader = new Downloader();
		downloader.setFiles(new GUIDownloaderFiles(localComputer));
		downloader.setRegistered(new GUIDownloaderRegistered(remoteComputer));
		
		channelFactory = new ChannelFactory(downloader, uploader);
		
		/***********************************************************************/
		localComputer.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		remoteComputer.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

		GUIFileChooserWrapper fileChooser = new GUIFileChooserWrapperCached();
		addLocalFiles.setOnAction(event -> {
			try {
				Window window = addLocalFiles.getScene().getWindow();
				List<File> files = fileChooser.getFiles(window);

				if (files != null) {
					FileBase<RemoteFile> registeredFiles = new FileBase<>();
					FileBase<LocalFileReadable> fileBase = uploader.getFiles();
					
					for (File file : files) {
						LocalFileReadable localFileReadable = new LocalFileReadable(file);
						RemoteFile remoteFile = new RemoteFile(localFileReadable.getFileId(),
															   localFileReadable.getFileSize(),
															   localFileReadable.getFile().getName());
						
						fileBase.add(localFileReadable);
						registeredFiles.add(remoteFile);
					}
					
					uploader.getRegistered().addAll(registeredFiles);
					if (channel != null && channel.isConnectionEstablished()) {
						channel.registerNewFiles(registeredFiles);	
					}
				}
			} catch (IOException | InterruptedException e) {
				e.printStackTrace();
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
									setConnection(c);
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
		
		removeFiles.setOnAction(event->{
			try {
				channel.stop();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
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
		
		addRemoteFiles.setOnAction(event->{
			FileBase<LocalFileWriteable> newFiles = new FileBase<>();
			
			for (RemoteFile registered : downloader.getRegistered()) {
				FileId fileId = registered.getFileId();
				File file = new File(config.getDownloadDirectory() + registered.getName());
				FileTotalSize fileTotalSize = registered.getFileSize().getTotalSize();
				Priority priority = new Priority(1);
				
				WriteableFileInfo wfi = new WriteableFileInfo(fileId, file,
															  fileTotalSize,
															  priority);
				
				try {
					newFiles.add(new LocalFileWriteable(wfi));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			downloader.getFiles().addAll(newFiles);
			try {
				channel.beginMessaging();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		});
	}

	public void setConnection(Connection c) {
		channel = channelFactory.create(c);
		channel.start();
		try {
			channel.registerNewFiles(uploader.getRegistered());
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void closeConnection() {
		try {
			if (channel != null) {
				channel.stop();
				channel = null;	
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
