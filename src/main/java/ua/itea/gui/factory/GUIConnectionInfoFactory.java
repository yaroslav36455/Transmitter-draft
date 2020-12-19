package ua.itea.gui.factory;

import java.io.IOException;

import ua.itea.gui.GUIConnectionInfo;

public interface GUIConnectionInfoFactory {
	public GUIConnectionInfo create() throws IOException;
}
