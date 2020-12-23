package ua.itea.gui;

import java.io.IOException;
import java.net.BindException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import javax.net.ServerSocketFactory;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
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
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import ua.itea.gui.factory.GUIChannelFactory;
import ua.itea.gui.factory.GUIChannelVBoxFactory;
import ua.itea.gui.factory.GUIConnectionInfoFactory;
import ua.itea.gui.factory.GUIConnectionInfoFactory;
import ua.itea.gui.factory.GUIIncomingConnectionDialogFactory;
import ua.itea.gui.modellink.ClientGUIChannelSocketFactory;
import ua.itea.gui.modellink.GUIConnectionProvider;
import ua.itea.model.Channel;
import ua.itea.model.ChannelBase;
import ua.itea.model.ChannelFactory;
import ua.itea.model.ChannelProvider;
import ua.itea.model.ChannelProvider;
import ua.itea.model.Client;
import ua.itea.model.ClientChannelFactory;
import ua.itea.model.ClientSocketFactory;
import ua.itea.model.Server;
import ua.itea.model.ServerProvider;
import ua.itea.model.ServerChannelFactory;
import ua.itea.model.ServerFactory;
import ua.itea.model.ServerFactory;
import ua.itea.model.SocketFactory;

public class GUIApplicationImpl extends Application implements Initializable {
	private ServerFactory serverFactory;
	private Client client;
	private ChannelBase channelBase;
	
	private GUIIncomingConnectionDialogFactory gicdf;
	
	@FXML
	private MenuItem newChannel;
	@FXML
	private MenuItem startServer;
	@FXML
	private MenuItem newConnection;
	@FXML
	private MenuItem acceptConnection;
	@FXML
	private MenuItem exit;
	@FXML
	private FlowPane serverPane;
	@FXML
	private Button addServer;
	@FXML
	private TabPane tabPane;
	
	private GUIChannelFactory paneFacotry;
	
	public GUIApplicationImpl() {		
//		GUIConnectionInfoFactory gcif = new GUIConnectionInfoFactory();
//		gicdf = new GUIIncomingConnectionDialogFactory(gcif);
		
		paneFacotry = new GUIChannelVBoxFactory();
		
		channelBase = new ChannelBase();
		
//		ChannelProvider serverChannelProvider = new GUIServerChannelProvider(channelBase);
//		serverFactory = new ServerFactory(serverChannelProvider);
		
//		ChannelProvider clientChannelProvider = new ChannelProvider(channelBase, new ClientChannelFactory());
//		client = new Client(clientChannelProvider, new GUIClientSocketFactory());
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		GUIConnectionInfoFactory gcif = new GUIConnectionInfoFactory();
		try {
			serverFactory = new ServerFactory(new GUIConnectionProvider(gcif.create()));
		} catch (IOException e) {
			e.printStackTrace();
		}

		newChannel.setOnAction(event -> {
			try {
				GUIChannel gui = paneFacotry.create();
				GUIChannelController gcc = gui.getController();
				Node node = gui.getNode();

				Tab tab = new Tab();
				Text channelName = gcc.getName();

				tab.textProperty().bindBidirectional(channelName.textProperty());
				tab.setText("Channel");
				tab.setContent(node);
				tabPane.getTabs().add(tab);
				tabPane.getSelectionModel().selectLast();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		});

		newConnection.setOnAction(event -> {
			if (client == null) {
				try {
					client.createChannel();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		
		acceptConnection.setOnAction(e->{
			try {
				GUIIncomingConnectionDialog dialog = gicdf.create();
				Optional<Channel> opt = dialog.showAndWait();
				
				if(opt.isPresent()) {
					System.out.println("present");
				} else {
					System.out.println("not present");
				}
			} catch (IOException e1) {
				e1.printStackTrace();
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

		stage.setOnCloseRequest(event->closeAllServers());
		
		stage.setScene(new Scene(root));
		stage.setMinWidth(800);
		stage.setMinHeight(600);
		stage.setMaximized(true);
		stage.setTitle("Transmitter");
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
