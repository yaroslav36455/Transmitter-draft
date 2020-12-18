package ua.itea.gui;

import java.io.File;
import java.util.List;

import javafx.stage.FileChooser;
import javafx.stage.Window;

public class GUIFileChooserWrapperCached implements GUIFileChooserWrapper {
	private FileChooser fileChooser;

	@Override
	public List<File> getFiles(Window ownerWindow) {
		List<File> files = null;
		
		if (fileChooser == null) {
			fileChooser = new FileChooser();
			fileChooser.setTitle("Select Files");
			fileChooser.setInitialDirectory(new File("/"));
		}
		
		files = fileChooser.showOpenMultipleDialog(ownerWindow);
		if(files != null) {
			fileChooser.setInitialDirectory(files.get(0).getParentFile());
		}
		
		return files;
	}

}