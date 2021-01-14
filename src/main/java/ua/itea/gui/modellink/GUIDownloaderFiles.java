package ua.itea.gui.modellink;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableView;
import ua.itea.gui.GUIFileWriteableTreeTableRow;
import ua.itea.gui.GUITreeTableRow;
import ua.itea.model.FileBase;
import ua.itea.model.FileId;
import ua.itea.model.LocalFileWriteable;

public class GUIDownloaderFiles extends FileBase<LocalFileWriteable> {
	private static final long serialVersionUID = -6644542984300523435L;
	private TreeTableView<GUITreeTableRow> treeTable;
	
	public GUIDownloaderFiles(TreeTableView<GUITreeTableRow> treeTable) {
		this.treeTable = treeTable;
	}
	
	@Override
	public List<LocalFileWriteable> addAll(List<LocalFileWriteable> localFiles) {
		List<LocalFileWriteable> added = super.addAll(localFiles);
		List<TreeItem<GUITreeTableRow>> rows = new ArrayList<>(added.size());
		
		for (LocalFileWriteable localFileWriteable : added) {
			rows.add(new TreeItem<>((GUIFileWriteableTreeTableRow) localFileWriteable));
		}
		
		treeTable.getRoot().getChildren().addAll(rows);
		return added;
	}
	
	@Override
	public List<LocalFileWriteable> removeAll(List<FileId> idList) {
		List<LocalFileWriteable> removed = super.removeAll(idList);
		
		for (LocalFileWriteable file : removed) {
			treeTable.getRoot().getChildren()
				.removeIf(lfw->lfw.getValue().getFileId().get() == file.getFileId().get());	
		}
		
		return removed;
	}

	@Override
	public void clear() {
		super.clear();
		treeTable.getRoot().getChildren().clear();
	}
}
