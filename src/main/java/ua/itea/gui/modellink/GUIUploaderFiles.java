package ua.itea.gui.modellink;

import java.io.IOException;

import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableView;
import ua.itea.gui.GUILocalFileRow;
import ua.itea.model.FileBase;
import ua.itea.model.FileSize;
import ua.itea.model.LocalFileReadable;

public class GUIUploaderFiles extends FileBase<LocalFileReadable> {
	private static final long serialVersionUID = 7226371787777760269L;
	private TableView<GUILocalFileRow> localComputer;
	
	public GUIUploaderFiles(TableView<GUILocalFileRow> localComputer) {
		this.localComputer = localComputer;
	}
	
	public boolean add(LocalFileReadable localFile) {
		boolean result = super.add(localFile);
		
		if (result) {
			
			try {
				FileSize fs = localFile.getFileSize();
				String fileName = localFile.getFile().getName();
				String filePath = localFile.getFile().getPath();
				double progress = fs.getFilledSize().getSize() / (double) fs.getTotalSize().getSize();
				ProgressBar progressBar = new ProgressBar(progress);

				GUILocalFileRow lfr = new GUILocalFileRow(fileName, filePath, fs, progressBar);
				localComputer.getItems().add(lfr);
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}	
		}
		
		return result;
	}
	
	public boolean addAll(FileBase<LocalFileReadable> localFiles) {
		boolean result = true;
		
		for (LocalFileReadable localFileReadable : localFiles) {
			result = result && add(localFileReadable);
		}
		
		return result;
	}
	
	public void clear() {
		super.clear();
		localComputer.getItems().clear();
	}
}
