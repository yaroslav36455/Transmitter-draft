package ua.itea.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ModifiableObservableListBase;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.text.Text;
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
			channels.removeIf(guiChannel -> guiChannel.getNode() == tab.getContent());
		});
		
		tabPane.getTabs().add(tab);
		tabPane.getSelectionModel().selectLast();
		
		channels.add(gui);

		return gui;
	}
}
