package ua.itea.gui;

import java.io.IOException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import ua.itea.gui.factory.GUIChannelFactory;

public class GUIChannelProvider {
	private GUIChannelFactory channelFactory;
	private ObservableList<GUIChannel> channels;
	private TabPane tabPane;

	public GUIChannelProvider(TabPane tabPane, GUIChannelFactory channelFactory) {
		this.tabPane = tabPane;
		this.channelFactory = channelFactory;
		this.channels = FXCollections.observableArrayList();
	}

	public ObservableList<GUIChannel> getChannels() {
		return channels;
	}

	public TabPane getTabPane() {
		return tabPane;
	}

	public GUIChannel create() throws IOException {
		GUIChannel gui = channelFactory.create();
		GUIChannelController gcc = gui.getController();
		Node node = gui.getNode();

		Tab tab = new Tab();

		tab.textProperty().bindBidirectional(gcc.getName().textProperty());
		tab.setText("Channel");
		tab.setContent(node);
		tab.setOnClosed(e -> {
			channels.removeIf(guiChannel -> {
				boolean found = guiChannel.getNode() == tab.getContent();
				
				if (found) {
					guiChannel.getController().close();
				}
				
				return found;
			});
		});
		
		tabPane.getTabs().add(tab);
		tabPane.getSelectionModel().selectLast();
		
		channels.add(gui);

		return gui;
	}

	public void closeAll() {
		for (Tab tab : tabPane.getTabs()) {
			ActionEvent.fireEvent(tab, new Event(Tab.CLOSED_EVENT));
		}
		
		tabPane.getTabs().clear();
	}
}
