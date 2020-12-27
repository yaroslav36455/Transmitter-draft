package ua.itea.gui;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.control.TabPane;
import javafx.scene.control.Button;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import ua.itea.gui.factory.GUIAddContactDialogFactory;
import ua.itea.gui.factory.GUIChannelVBoxFactory;
import ua.itea.gui.factory.GUIConnectionInfoFactory;
import ua.itea.gui.factory.GUIIncomingConnectionDialogFactory;
import ua.itea.model.ConnectionProvider;
import ua.itea.model.ConnectionServer;
import ua.itea.model.Server;
import ua.itea.model.ServerFactory;

public class GUIApplicationImpl extends Application implements Initializable {
	private ServerFactory serverFactory;
	
	private GUIIncomingConnectionDialogFactory gicdf;
	
	@FXML
	private MenuItem newChannel;
	@FXML
	private MenuItem exit;
	@FXML 
	private MenuItem contacts;
	@FXML
	private FlowPane serverPane;
	@FXML
	private Button addServer;
	@FXML
	private TabPane tabPane;
	
	private GUIChannelProvider gcp;
	
	public GUIApplicationImpl() {		
//		GUIConnectionInfoFactory gcif = new GUIConnectionInfoFactory();
//		gicdf = new GUIIncomingConnectionDialogFactory(gcif);
		
//		channelBase = new ChannelBase();
		
//		ChannelProvider serverChannelProvider = new GUIServerChannelProvider(channelBase);
//		serverFactory = new ServerFactory(serverChannelProvider);
		
//		ChannelProvider clientChannelProvider = new ChannelProvider(channelBase, new ClientChannelFactory());
//		client = new Client(clientChannelProvider, new GUIClientSocketFactory());
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		gcp = new GUIChannelProvider(tabPane, new GUIChannelVBoxFactory());
		GUIConnectionInfoFactory gcif = new GUIConnectionInfoFactory();
		try {
			GUIConnectionInfo gci = gcif.create();
			serverFactory = new ServerFactory(new ConnectionProvider() {
				
				@Override
				public void start(ConnectionServer c) {
					Platform.runLater(() -> {
						try {
							GUIConnectionInfoController controller = gci.getController();
							controller.getName().setText(c.readName());
							controller.getAddress().setText(c.getSocket().getInetAddress().getHostAddress());
							controller.getPort().setText(String.valueOf(c.getSocket().getPort()));

							GUIIncomingConnectionDialogFactory gicdf = new GUIIncomingConnectionDialogFactory(gci);
							GUIIncomingConnectionDialog dialog = gicdf.create();
							
							dialog.setGUIChannelProvider(gcp);
							
							Optional<GUIChannel> ch = dialog.showAndWait();
							
							if (ch.isPresent()) {
								c.accept();
								GUIChannelController contr = ch.get().getController();
								contr.setConnection(c);
							} else {
								c.reject();
								try {
									c.close();
								} catch (IOException ex) {
									ex.printStackTrace();
								}
							}

						} catch (IOException e) {
							e.printStackTrace();
							try {
								c.close();
							} catch (IOException ex) {
								ex.printStackTrace();
							}
						}
					});
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
		}

		newChannel.setOnAction(event -> {
			try {
				gcp.create();
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
		
		contacts.setOnAction(e->{
			try {
				GUIAddContactDialogFactory factory = new GUIAddContactDialogFactory();
				GUIContactDatabaseEditorDialog gcdd = new GUIContactDatabaseEditorDialog(factory);
				FXMLLoader loader = new FXMLLoader();
				loader.setLocation(this.getClass().getClassLoader().getResource("contact-database.fxml"));
				loader.setController(gcdd);
				loader.load();
				gcdd.show();	
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			
		});
		
		addServer.setOnAction(e -> {
			try {
				Server server = serverFactory.create();

				ObservableList<Node> ch = serverPane.getChildren();
				MenuItem mi = new MenuItem("Close");
				SplitMenuButton smb = new SplitMenuButton(mi);
				
				smb.setText(Integer.toString(server.getLocalPort()));
				ch.add(ch.size() - 1, smb);

				mi.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						server.close();
						ch.remove(smb);
					}
				});
			
			} catch(IOException ioe) {
				ioe.printStackTrace();
			}
		});
	}

	@Override
	public void start(Stage stage) throws Exception {
		ResourceBundle resources = ResourceBundle.getBundle("qwf");

		FXMLLoader loader = new FXMLLoader(GUIApplicationImpl.class.getClassLoader().getResource("window.fxml"),
				resources);
		loader.setController(this);

		Parent root = loader.load();
		
		stage.setScene(new Scene(root));
		stage.setMinWidth(800);
		stage.setMinHeight(600);
		stage.setMaximized(true);
		stage.setTitle("Transmitter");
		
		stage.setOnHidden(event->{
			gcp.closeAll();
			closeAllServers();
		});
		
		exit.setOnAction(e->stage.close());
		
		stage.show();
	}
	
	private void closeAllServers() {
		ObservableList<Node> ch = serverPane.getChildren();
		List<SplitMenuButton> arrary = new ArrayList<>();
		
		for (Node node : ch) {
			if (node instanceof SplitMenuButton) {
				arrary.add((SplitMenuButton) node);
			}
		}
		
		for (SplitMenuButton smb : arrary) {
			smb.getItems().get(0).fire();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
