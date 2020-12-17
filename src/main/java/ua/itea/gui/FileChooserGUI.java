package ua.itea.gui;

import java.io.File;
import java.util.List;

import javafx.stage.Window;
import ua.itea.model.LocalFile;

public interface FileChooserGUI {
	public List<File> getFiles(Window ownerWindow);
}
