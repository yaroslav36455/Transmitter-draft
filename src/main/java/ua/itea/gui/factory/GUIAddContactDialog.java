package ua.itea.gui.factory;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextField;
import javafx.scene.Node;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.util.Callback;
import ua.itea.db.Contact;

public class GUIAddContactDialog extends Dialog<Contact>
								 implements Initializable {
	@FXML
	private TextField name;
	@FXML
	private TextField address;
	@FXML
	private TextField port;
	
	private ButtonType okButton;
	private Node okButtonNode;
	
	int i = 0;
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		setTitle("New contact");
		
		okButton = new ButtonType("Ok", ButtonData.OK_DONE);
		getDialogPane().getButtonTypes().addAll(okButton, ButtonType.CANCEL);
		okButtonNode = getDialogPane().lookupButton(okButton); 
		okButtonNode.setDisable(true);
		
		name.setOnKeyTyped(e->setOkButtonState());
		address.setOnKeyTyped(e->setOkButtonState());
		port.setOnKeyTyped(e->setOkButtonState());
		
		setOnShown(e->name.requestFocus());
		name.setOnAction(e->address.requestFocus());
		address.setOnAction(e->port.requestFocus());
		port.setOnAction(e->{
			if (okButtonNode.isDisabled()) {
				name.requestFocus();
			} else {
				okButtonNode.fireEvent(new ActionEvent());
			}
		});
		
		setResultConverter(new Callback<ButtonType, Contact>() {
			
			@Override
			public Contact call(ButtonType param) {
				if (param == okButton) {
					return new Contact(name.getText(),
									   address.getText(),
									   Integer.valueOf(port.getText()));
				}
				
				return null;
			}
		});
	}
	
	private void setOkButtonState() {
		okButtonNode.setDisable(!isCorrect());
	}
	
	private boolean isCorrect() {
		boolean nameCorrect = false;
		boolean addressCorrect = false;
		boolean portCorrect = false;
		
		nameCorrect = !name.getText().trim().isEmpty();
		addressCorrect = !address.getText().trim().isEmpty();
		
		try {
			Integer.valueOf(port.getText());
			portCorrect = true;
		} catch(NumberFormatException ex) {
			
		}
		
		return nameCorrect && addressCorrect && portCorrect;
	}
}
