package ua.itea.gui;

import java.io.IOException;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionModel;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.util.Callback;
import ua.itea.model.Channel;
import ua.itea.model.ServerChannel;

public class GUIIncomingConnectionDialog extends Dialog<Channel> {
	private ListView<Channel> tabNameList;
	private CheckBox createNewChannel;
	private ButtonType acceptButtonType;

	public GUIIncomingConnectionDialog(GUIConnectionInfo gci) throws IOException {
		tabNameList = new ListView<>();
		acceptButtonType = new ButtonType("Allow");
		createNewChannel = new CheckBox("create new channel");
		
		createNewChannel.setSelected(false);
		tabNameList.setMaxHeight(200);
		
		Text text = new Text("Select free channel");
		text.setTextAlignment(TextAlignment.RIGHT);
		
		VBox vBox = new VBox(gci.getNode(), new BorderPane(text), tabNameList, createNewChannel);
		getDialogPane().setContent(vBox);
		getDialogPane().getButtonTypes().addAll(acceptButtonType, ButtonType.CANCEL);
		setTitle("Incoming connection");
		setResizable(true);

		setListeners();
	}

	public void setItems(ObservableList<Channel> items) {
		this.tabNameList.setItems(items);
	}

	private void setListeners() {
		SelectionModel<Channel> sm = tabNameList.getSelectionModel();
		Node acceptButton = getDialogPane().lookupButton(acceptButtonType);
		acceptButton.setDisable(true);

		createNewChannel.selectedProperty().addListener(e -> {
			if (createNewChannel.isSelected()) {
				tabNameList.setDisable(true);
				acceptButton.setDisable(false);
			} else {
				tabNameList.setDisable(false);

				listItemSelectionEvent(acceptButton, sm);
			}
		});

		sm.selectedIndexProperty().addListener(e -> {
			listItemSelectionEvent(acceptButton, sm);
		});

		setResultConverter(new Callback<ButtonType, Channel>() {

			@Override
			public Channel call(ButtonType param) {
				if (param == acceptButtonType) {
					return createNewChannel.isSelected() ? new ServerChannel()
							 							 : sm.getSelectedItem();	
				}
				
				return null;
			}
		});
	}

	private void listItemSelectionEvent(Node acceptButton,
										SelectionModel<Channel> sm) {
		if (sm.getSelectedIndex() == -1) {
			acceptButton.setDisable(true);
		} else {
			acceptButton.setDisable(false);
		}
	}
}
