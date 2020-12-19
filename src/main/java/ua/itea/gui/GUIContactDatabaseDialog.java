package ua.itea.gui;

import java.net.URL;
import java.util.ResourceBundle;

import com.sun.glass.ui.Screen;

import javafx.event.Event;
import javafx.event.EventDispatchChain;
import javafx.event.EventDispatcher;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.TableView;
import javafx.scene.control.TableView.ResizeFeatures;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.stage.WindowEvent;
import javafx.util.Callback;
import ua.itea.db.Contact;

public class GUIContactDatabaseDialog extends Dialog<Contact>
									  implements Initializable {
	@FXML private TableView<Contact> contacts;
	@FXML private Button selectButton;
	@FXML private Button cancelButton;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		Window window = getDialogPane().getScene().getWindow();
		
		setTitle("Contacts");
		setResizable(true);
		
		contacts.getItems().add(new Contact("Name 1", "192.168.0.3", 2565));
		contacts.getItems().add(new Contact("Name 2", "192.168.0.5", 45632));
		contacts.setOnMouseClicked(event->{
			if (contacts.getSelectionModel().getSelectedIndex() == -1) {
				selectButton.setDisable(true);
			} else {
				selectButton.setDisable(false);
			}
		});
		
		selectButton.setDisable(true);
		selectButton.setOnAction(event->{
			setResult(contacts.getSelectionModel().getSelectedItem());
			window.hide();
		});
		
		cancelButton.setOnAction(event->{
			window.fireEvent(new WindowEvent(window,
											 WindowEvent.WINDOW_CLOSE_REQUEST));
		});
		
		window.setOnCloseRequest(e->{
			setResult(null);
		});
		
		setOnShowing(event->{
			window.sizeToScene();
		});
	}
}
