package ua.itea.gui;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.SelectionModel;
import javafx.scene.control.TableView;
import javafx.stage.Window;
import javafx.util.Callback;
import ua.itea.db.Connector;
import ua.itea.db.ConnectorImpl;
import ua.itea.db.Contact;
import ua.itea.db.ContactDatabase;

public class GUIContactDatabaseDialog extends Dialog<Contact>
									  implements Initializable {
	@FXML private TableView<Contact> contacts;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		Window window = getDialogPane().getScene().getWindow();
		SelectionModel<Contact> selectionModel = contacts.getSelectionModel();
		ButtonType select = new ButtonType("Select");
		
		setTitle("Contacts");
		setResizable(true);
		getDialogPane().setPrefSize(300, 300);
			
		getDialogPane().getButtonTypes().addAll(select, ButtonType.CANCEL);
		
		Node selectNode = getDialogPane().lookupButton(select);
		selectNode.setDisable(true);
		
		selectionModel.selectedIndexProperty().addListener(e->
			selectNode.setDisable(selectionModel.getSelectedIndex() == -1)
		);
		
		setOnShowing(event->{
			try {
				Connector connector = new ConnectorImpl();
				ContactDatabase contactDatabase = new ContactDatabase(connector.getConnection());
				
				contacts.getItems().addAll(contactDatabase.read(connector.getConnection()));
				
				window.sizeToScene();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		});
		
		setResultConverter(new Callback<ButtonType, Contact>() {

			@Override
			public Contact call(ButtonType param) {
				Contact result = null;
				
				if (param == select) {
					result = selectionModel.getSelectedItem();
				}
				
				return result;
			}
		});
		
		setOnCloseRequest(e->{
			contacts.getItems().clear();
		});
	}
}
