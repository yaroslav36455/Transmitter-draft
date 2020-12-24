package ua.itea.gui;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableView;
import javafx.scene.control.TableView.TableViewSelectionModel;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import ua.itea.db.Connector;
import ua.itea.db.ConnectorImpl;
import ua.itea.db.Contact;
import ua.itea.db.ContactDatabase;
import ua.itea.gui.factory.GUIAddContactDialog;
import ua.itea.gui.factory.GUIAddContactDialogFactory;

public class GUIContactDatabaseEditorDialog extends Dialog<ButtonType>
											implements Initializable {
	@FXML private TableView<Contact> contacts;
	private GUIAddContactDialogFactory gacdf; 
	private Button add;
	private Button remove;
	private Connector connector;
	private ContactDatabase contactDatabase;
	
	public GUIContactDatabaseEditorDialog(GUIAddContactDialogFactory gacdf) throws SQLException {
		this.gacdf = gacdf;
		this.connector = new ConnectorImpl();
		this.contactDatabase = new ContactDatabase(connector.getConnection());;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		setTitle("Contacts");
		setResizable(true);
		getDialogPane().setPrefSize(300, 300);
		
		add = new Button("Add");
		remove = new Button("Remove");
		
		contacts.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		
		BorderPane borderPane = new BorderPane(contacts);
		borderPane.setBottom(new FlowPane(add, remove));
		getDialogPane().setContent(borderPane);
		getDialogPane().getButtonTypes().addAll(ButtonType.CLOSE);
		
		setOnShowing(event->{
			try {
				contacts.getItems().addAll(contactDatabase.read(connector.getConnection()));
			} catch (SQLException e) {
				e.printStackTrace();
			}
		});
		
		add.setOnAction(e->{
			try {
				GUIAddContactDialog gcdfd = gacdf.create();
				Optional<Contact> opt = gcdfd.showAndWait();
				
				if (opt.isPresent()) {
					contactDatabase.insert(connector.getConnection(), opt.get());
					contacts.getItems().add(opt.get());
				}
			} catch (IOException | SQLException ex) {
				ex.printStackTrace();
			}
		});
		
		remove.setOnAction(e->{
			TableViewSelectionModel<Contact> sm = contacts.getSelectionModel();
			List<Contact> selected = new ArrayList<>(sm.getSelectedItems());
			
			try {
				contactDatabase.remove(connector.getConnection(), selected);
				sm.clearSelection();
				contacts.getItems().removeAll(selected);
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		});
		
		contacts.getSelectionModel().selectedIndexProperty().addListener(e->{
			remove.setDisable(contacts.getSelectionModel().getSelectedIndex() == -1);
		});
	}
}
