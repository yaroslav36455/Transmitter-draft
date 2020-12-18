package ua.itea.gui;

import java.io.File;
import java.util.List;

import javafx.stage.Window;

public interface GUIFileChooserWrapper {
	public List<File> getFiles(Window ownerWindow);
}
