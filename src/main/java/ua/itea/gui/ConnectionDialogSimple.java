package ua.itea.gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.util.Callback;

public class ConnectionDialogSimple extends Dialog<ConnectionInfo> 
									implements Initializable {
	private static ConnectionDialogSimple CREATE_CONNECTION_DIALOG;
	@FXML private TextField addressTextField;
	@FXML private TextField portTextField;
	@FXML private TextField nameTextField;
	private ButtonType createButton;
	private ProgressIndicator progressIndicator;
	
	public static ConnectionDialogSimple get() throws IOException {
		if (CREATE_CONNECTION_DIALOG == null) {
			CREATE_CONNECTION_DIALOG = new ConnectionDialogSimple();
			
			ClassLoader classLoader = ApplicationImpl.class.getClassLoader();
			URL url = classLoader.getResource("create-connection.fxml");
			FXMLLoader loader = new FXMLLoader(url);
			
			loader.setController(CREATE_CONNECTION_DIALOG);
			CREATE_CONNECTION_DIALOG.getDialogPane().setContent(loader.load());
		}

		return CREATE_CONNECTION_DIALOG;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		createButton = new ButtonType("Create");
		progressIndicator = new ProgressIndicator();
		
		setTitle("Connection");
		setGraphic(progressIndicator);
		getDialogPane().getButtonTypes().addAll(createButton, ButtonType.CANCEL);
		
		setResultConverter(new Callback<ButtonType, ConnectionInfo>() {
			
			@Override
			public ConnectionInfo call(ButtonType param) {
				ConnectionInfo connectionInfo = null;
				System.out.println("result");
				
				if (param == createButton) {
					String address = addressTextField.getText();
					String port = portTextField.getText();
					String name = nameTextField.getText();

					connectionInfo = new ConnectionInfo(address, port, name);
				}
				
				return connectionInfo;
			}
		});

		setOnShowing(event->refresh());
		
//		addressTextField.setOnInputMethodTextChanged(new EventHandler<InputMethodEvent>() {
//
//			@Override
//			public void handle(InputMethodEvent event) {
//				System.out.println("input");
//			}
//		});
		
//		setOnCloseRequest(event->{
//			System.out.println("close Event");
//			event.consume();
//			progressIndicator.setVisible(true);
//		});
//		
//		this.getDialogPane().getScene().getWindow().setOnCloseRequest(event->{
//			event.consume();
//		});
		
//		BooleanProperty property = new SimpleBooleanProperty(false);
//		getDialogPane().lookupButton(ButtonType.CANCEL).setOnMouseClicked(e->property.set(!property.get()));
//		getDialogPane().lookupButton(addButton).disableProperty().bind(property);
//		progressIndicator.visibleProperty().bind(property);
		
//		nameTextField.setOnKeyTyped(event->{
//			addressTextField.requestFocus();
//			System.out.println("event");
//			nameTextField.setFocusTraversable(false);
//			addressTextField.setFocusTraversable(false);
//		});
		
		getDialogPane().lookupButton(createButton).setOnMouseClicked(event->{
			progressIndicator.setVisible(true);
			setHeaderText("Connection...");
		});
	}
	
	private void refresh() {
		addressTextField.requestFocus();
		addressTextField.setText("");
		portTextField.setText("");
		nameTextField.setText("");
		progressIndicator.setVisible(false);
		setHeaderText("Create connection");
	}
}
