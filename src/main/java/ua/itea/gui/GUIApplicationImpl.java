package ua.itea.gui;

import java.awt.FileDialog;
import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

import javafx.application.Application;
import javafx.event.Event;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import ua.itea.gui.factory.GUIConnectionDialogFactory;
import ua.itea.gui.factory.GUIConnectionDialogSimpleCachedFactory;
import ua.itea.gui.factory.GUIChannel;
import ua.itea.gui.factory.GUIChannelFactory;
import ua.itea.gui.factory.GUIChannelSplitPaneFactory;
import ua.itea.model.ChannelBase;
import ua.itea.model.ChannelFactory;
import ua.itea.model.ChannelProviderImpl;
import ua.itea.model.Client;
import ua.itea.model.ClientChannelFactory;
import ua.itea.model.ClientSocketFactory;
import ua.itea.model.Server;
import ua.itea.model.ServerChannelFactory;
import ua.itea.model.SocketFactory;

public class GUIApplicationImpl extends Application implements Initializable {
	private Server server;
	private Client client;
	private Stage mainStage;
	@FXML private MenuItem startServer;
	@FXML private MenuItem newConnection;
	@FXML private MenuItem exit;
    @FXML private TabPane tabPane;
    
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		GUIChannelFactory paneFacotry = new GUIChannelSplitPaneFactory();
		GUIConnectionDialogFactory dialogFactory = new GUIConnectionDialogSimpleCachedFactory();
		
		ChannelBase channelBase = new ChannelBase();
		newConnection.setOnAction(event->{
//			try {
//				Optional<GUIConnectionInfo> opt = dialogFactory.create().showAndWait();
//	
//				if (opt.isPresent()) {
//					GUIConnectionInfo conn = opt.get();
//					
//					System.out.println("Address: " + conn.getAddress());
//					System.out.println("Port: " + conn.getPort());
//					System.out.println("Name: " + conn.getName());
////					System.out.println(conn.getClass());
//					
//					GUIChannel gui = paneFacotry.create();
//					gui.getController();
//					Tab tab = new Tab();
//					tab.setText(conn.getName());
//					tab.setContent(gui.getNode());
//					tabPane.getTabs().add(tab);
//				}
//				
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
			
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
		
		startServer.setOnAction(e->{
			if(server == null) {
				ChannelFactory channelFactory = new ServerChannelFactory();
				ChannelProviderImpl channelProvider = new ChannelProviderImpl(channelBase, channelFactory);
				
				server = new Server(44444, channelProvider);
			}
		});
	}

	@Override
	public void start(Stage stage) throws Exception {
		mainStage = stage;
		ResourceBundle resources = ResourceBundle.getBundle("qwf");
		
		FXMLLoader loader = new FXMLLoader(GUIApplicationImpl.class.getClassLoader().getResource("window.fxml"), resources);
		loader.setController(this);
		
		BorderPane borderPane = loader.load();
		
		stage.setScene(new Scene(borderPane));
		stage.setMinWidth(800);
		stage.setMinHeight(600);
		stage.setMaximized(true);
		stage.setTitle("Transmitter");
		stage.show();
	}
	
    public static void main(String[] args) {
        launch(args);
    }
}
