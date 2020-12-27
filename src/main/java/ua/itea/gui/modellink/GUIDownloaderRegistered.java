package ua.itea.gui.modellink;

import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableView;
import ua.itea.gui.GUIRemoteFileRow;
import ua.itea.model.FileBase;
import ua.itea.model.FileSize;
import ua.itea.model.RemoteFile;

public class GUIDownloaderRegistered extends FileBase<RemoteFile> {
	private static final long serialVersionUID = 2645413820373500920L;
	private TableView<GUIRemoteFileRow> remoteComputer;
	
	public GUIDownloaderRegistered(TableView<GUIRemoteFileRow> remoteComputer) {
		this.remoteComputer = remoteComputer;
	}

	public boolean add(RemoteFile remoteFile) {
		boolean result = super.add(remoteFile);
		
		if (result) {
			FileSize fs = remoteFile.getFileSize();
			String fileName = remoteFile.getName();
			double progress = fs.getFilledSize().getSize() / (double) fs.getTotalSize().getSize();
			ProgressBar progressBar = new ProgressBar(progress);

			GUIRemoteFileRow rfr = new GUIRemoteFileRow(fileName, fs, progressBar);
			remoteComputer.getItems().add(rfr);
		}
		
		return result;
	}
	
	public boolean addAll(FileBase<RemoteFile> remoteFiles) {
		boolean result = true;
		
		for (RemoteFile remoteFile : remoteFiles) {
			result = result && add(remoteFile);
		}
		
		return result;
	}
	
	public void clear() {
		super.clear();
		remoteComputer.getItems().clear();
	}
}
