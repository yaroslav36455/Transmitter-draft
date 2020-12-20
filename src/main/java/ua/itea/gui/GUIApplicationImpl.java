package ua.itea.gui;

import java.io.IOException;
import java.net.BindException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;

import javafx.application.Application;
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
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import ua.itea.gui.factory.GUIChannelFactory;
import ua.itea.gui.factory.GUIChannelVBoxFactory;
import ua.itea.model.ChannelBase;
import ua.itea.model.ChannelFactory;
import ua.itea.model.ChannelProvider;
import ua.itea.model.ChannelProviderImpl;
import ua.itea.model.Client;
import ua.itea.model.ClientChannelFactory;
import ua.itea.model.ClientSocketFactory;
import ua.itea.model.Server;
import ua.itea.model.ServerProvider;
import ua.itea.model.ServerChannelFactory;
import ua.itea.model.ServerFactory;
import ua.itea.model.ServerImplFactory;
import ua.itea.model.SocketFactory;

public class GUIApplicationImpl extends Application implements Initializable {
	private ServerFactory serverFactory;
	private ChannelProvider channelProvider;
	private Client client;
	
	private Stage mainStage;
	@FXML
	private MenuItem newChannel;
	@FXML
	private MenuItem startServer;
	@FXML
	private MenuItem newConnection;
	@FXML
	private MenuItem exit;
	@FXML
	private FlowPane serverPane;
	@FXML
	private Button addServer;
	@FXML
	private TabPane tabPane;
	
	public GUIApplicationImpl() {
		channelProvider = new ChannelProviderImpl(new ChannelBase(), new ServerChannelFactory());
		serverFactory = new ServerImplFactory(channelProvider);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		GUIChannelFactory paneFacotry = new GUIChannelVBoxFactory();
		ChannelBase channelBase = new ChannelBase();

		newChannel.setOnAction(event -> {
			try {
				GUIChannel gui = paneFacotry.create();
				GUIChannelController gcc = gui.getController();
				Node node = gui.getNode();

				Tab tab = new Tab();
				Text channelName = gui.getController().getName();

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
				ChannelFactory channelFactory = new ClientChannelFactory();
				ChannelProviderImpl channelProviderImpl = new ChannelProviderImpl(channelBase, channelFactory);
				SocketFactory socketFactory = new ClientSocketFactory();

				client = new Client(channelProviderImpl, socketFactory);
				try {
					client.createChannel();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		
		addServer.setOnAction(e -> {
			try {
				Server server = serverFactory.create();

				ObservableList<Node> ch = serverPane.getChildren();
				MenuItem mi = new MenuItem("Close");
				SplitMenuButton smb = new SplitMenuButton(mi);
				
				smb.setText(Integer.toString(server.getPort()));
				ch.add(ch.size() - 1, smb);

				mi.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						server.stop();
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
		mainStage = stage;
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
		stage.show();
		
		stage.setOnCloseRequest(event->{
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
		});
	}

	public static void main(String[] args) {
		launch(args);
	}
}
