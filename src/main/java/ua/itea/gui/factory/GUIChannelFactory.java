package ua.itea.gui.factory;

import java.io.IOException;

import ua.itea.gui.GUIChannel;

public interface GUIChannelFactory {
	public GUIChannel create() throws IOException;
}
