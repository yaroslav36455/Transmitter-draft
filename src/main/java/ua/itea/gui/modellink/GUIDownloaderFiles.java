package ua.itea.gui.modellink;

import java.io.IOException;

import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableView;
import ua.itea.gui.GUILocalFileRow;
import ua.itea.model.FileBase;
import ua.itea.model.FileSize;
import ua.itea.model.LocalFileWriteable;

public class GUIDownloaderFiles extends FileBase<LocalFileWriteable> {
	private static final long serialVersionUID = -6644542984300523435L;
	private TableView<GUILocalFileRow> localComputer;
	
	public GUIDownloaderFiles(TableView<GUILocalFileRow> localComputer) {
		this.localComputer = localComputer;
	}
	
	public boolean add(LocalFileWriteable localFile) {
		boolean result = super.add(localFile);
		
		if (result) {
			
			try {
				FileSize fs = localFile.getFileSize();
				String fileName = localFile.getFile().getName();
				String filePath = localFile.getFile().getParent();
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
	
	public boolean addAll(FileBase<LocalFileWriteable> localFiles) {
		boolean result = true;
		
		for (LocalFileWriteable localFileWriteable : localFiles) {
			result = result && add(localFileWriteable);
		}
		
		return result;
	}
	
	public void clear() {
		super.clear();
		localComputer.getItems().clear();
	}
}
