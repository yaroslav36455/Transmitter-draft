package ua.itea.gui.modellink;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableView;
import ua.itea.gui.GUIFileReadableTreeTableRow;
import ua.itea.gui.GUITreeTableRow;
import ua.itea.model.FileBase;
import ua.itea.model.FileId;
import ua.itea.model.LocalFileReadable;

public class GUIUploaderFiles extends FileBase<LocalFileReadable> {
	private static final long serialVersionUID = 7226371787777760269L;
	private TreeTableView<GUITreeTableRow> treeTable;
	
	public GUIUploaderFiles(TreeTableView<GUITreeTableRow> treeTable) {
		this.treeTable = treeTable;
	}
	
	@Override
	public List<LocalFileReadable> addAll(List<LocalFileReadable> localFiles) {
		List<LocalFileReadable> added = super.addAll(localFiles);
		List<TreeItem<GUITreeTableRow>> rows = new ArrayList<>(added.size());
		
		for (LocalFileReadable localFileReadable : added) {
			rows.add(new TreeItem<>((GUIFileReadableTreeTableRow) localFileReadable));
		}
		
		treeTable.getRoot().getChildren().addAll(rows);
		return added;
	}
	
	@Override
	public List<LocalFileReadable> removeAll(List<FileId> idList) {
		List<LocalFileReadable> removed = super.removeAll(idList);
		
		for (LocalFileReadable file : removed) {
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
