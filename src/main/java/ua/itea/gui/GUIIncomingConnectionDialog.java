package ua.itea.gui;

import java.io.IOException;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionModel;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.util.Callback;
import ua.itea.model.Channel;
import ua.itea.model.ServerChannel;

public class GUIIncomingConnectionDialog extends Dialog<Tab> {
	private ListView<Tab> tabNameList;
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
		
		tabNameList.setCellFactory(new Callback<ListView<Tab>, ListCell<Tab>>() {

			@Override
			public ListCell<Tab> call(ListView<Tab> param) {
				return new ListCell<Tab>() {
					@Override
					protected void updateItem(Tab item, boolean empty) {
						super.updateItem(item, empty);
						
						if (item != null) {
							super.setText(item.getText());	
						}
					}
				};
			}
		});
	}

	public void setItems(TabPane tabPane) {
		this.tabNameList.setItems(tabPane.getTabs());
		SelectionModel<Tab> tabPaneSM = tabPane.getSelectionModel();
		SelectionModel<Tab> listViewSM = tabNameList.getSelectionModel();
		
		listViewSM.selectedIndexProperty().addListener(e->{
			int selected = listViewSM.getSelectedIndex();
			
			if (selected != -1) {
				tabPaneSM.select(selected);
			}
		});
	}

	private void setListeners() {
		SelectionModel<Tab> sm = tabNameList.getSelectionModel();
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

		setResultConverter(new Callback<ButtonType, Tab>() {

			@Override
			public Tab call(ButtonType param) {
				if (param == acceptButtonType) {
					return createNewChannel.isSelected() ? new Tab("New Tab Text")
							 							 : sm.getSelectedItem();	
				}
				
				return null;
			}
		});
	}

	private void listItemSelectionEvent(Node acceptButton,
										SelectionModel<Tab> sm) {
		if (sm.getSelectedIndex() == -1) {
			acceptButton.setDisable(true);
		} else {
			acceptButton.setDisable(false);
		}
	}
}
