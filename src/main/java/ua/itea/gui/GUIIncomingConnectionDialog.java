package ua.itea.gui;

import java.io.IOException;

import javafx.scene.Node;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionModel;
import javafx.scene.control.Tab;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.util.Callback;

public class GUIIncomingConnectionDialog extends Dialog<GUIChannel> {
	private GUIChannelProvider gcp;
	private ListView<GUIChannel> tabNameList;
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

		tabNameList.setCellFactory(new Callback<ListView<GUIChannel>, ListCell<GUIChannel>>() {

			@Override
			public ListCell<GUIChannel> call(ListView<GUIChannel> param) {
				return new ListCell<GUIChannel>() {
					@Override
					protected void updateItem(GUIChannel item, boolean empty) {
						super.updateItem(item, empty);
						
						if (item != null) {
							Text name = item.getController().getName();
							textProperty().bindBidirectional(name.textProperty());
						}
					}
				};
			}
		});
	}

	public void setGUIChannelProvider(GUIChannelProvider gcp) {
		this.gcp = gcp;
		this.tabNameList.setItems(gcp.getChannels());
		SelectionModel<Tab> tabPaneSM = gcp.getTabPane().getSelectionModel();
		SelectionModel<GUIChannel> listViewSM = tabNameList.getSelectionModel();

		listViewSM.selectedIndexProperty().addListener(e -> {
			int selected = listViewSM.getSelectedIndex();
			
			if (selected != -1) {
				tabPaneSM.select(selected);
			}
		});
	}

	private void setListeners() {
		SelectionModel<GUIChannel> sm = tabNameList.getSelectionModel();
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

		setResultConverter(new Callback<ButtonType, GUIChannel>() {

			@Override
			public GUIChannel call(ButtonType param) {
				try {
					if (param == acceptButtonType) {
						return createNewChannel.isSelected() ? gcp.create()
															 : sm.getSelectedItem();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}

				return null;
			}
		});
	}

	private void listItemSelectionEvent(Node acceptButton, SelectionModel<GUIChannel> sm) {
		if (sm.getSelectedIndex() == -1) {
			acceptButton.setDisable(true);
		} else {
			acceptButton.setDisable(false);
		}
	}
}
