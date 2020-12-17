package ua.itea.gui;

import java.awt.FileDialog;
import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.URL;
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
import ua.itea.gui.factory.ConnectionDialogGUIFactory;
import ua.itea.gui.factory.ConnectionDialogSimpleCachedGUIFactory;
import ua.itea.gui.factory.ConnectionGUI;
import ua.itea.gui.factory.ConnectionGUIFactory;
import ua.itea.gui.factory.ConnectionSplitPaneGUIFactory;
import ua.itea.model.Client;
import ua.itea.model.Server;

public class ApplicationImpl extends Application implements Initializable {
	private Server server;
	private Client client;
	private Stage mainStage;
	@FXML private MenuItem startServer;
	@FXML private MenuItem newConnection;
	@FXML private MenuItem exit;
    @FXML private TabPane tabPane;
    
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		ConnectionGUIFactory paneFacotry = new ConnectionSplitPaneGUIFactory();
		ConnectionDialogGUIFactory dialogFactory = new ConnectionDialogSimpleCachedGUIFactory();
		
		newConnection.setOnAction(event->{
			try {
				Optional<ConnectionInfo> opt = dialogFactory.create().showAndWait();
	
				if (opt.isPresent()) {
					ConnectionInfo conn = opt.get();
					
					System.out.println("Address: " + conn.getAddress());
					System.out.println("Port: " + conn.getPort());
					System.out.println("Name: " + conn.getName());
//					System.out.println(conn.getClass());
					
					ConnectionGUI gui = paneFacotry.create();
					gui.getController();
					Tab tab = new Tab();
					tab.setText(conn.getName());
					tab.setContent(gui.getNode());
					tabPane.getTabs().add(tab);
				}
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
		
		startServer.setOnAction(e->{
			if(server == null) {
				//server = new Server(44444, channelProvider);
			}
		});
	}

	@Override
	public void start(Stage stage) throws Exception {
		mainStage = stage;
		ResourceBundle resources = ResourceBundle.getBundle("qwf");
		
		FXMLLoader loader = new FXMLLoader(ApplicationImpl.class.getClassLoader().getResource("window.fxml"), resources);
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
