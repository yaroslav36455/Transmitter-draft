package ua.itea.gui.factory;

import java.io.File;

import ua.itea.gui.GUIFileWriteableTreeTableRow;
import ua.itea.model.FileId;
import ua.itea.model.LocalFileWriteable;
import ua.itea.model.MemorySize;
import ua.itea.model.Priority;
import ua.itea.model.RemoteFileRegistered;
import ua.itea.model.factory.LocalFileWriteableFactory;

public class GUIFileWriteableTreeTableRowFactory extends LocalFileWriteableFactory {

	public GUIFileWriteableTreeTableRowFactory(String defaultPath, Priority defaultPriority) {
		super(defaultPath, defaultPriority);
	}

	@Override
	public LocalFileWriteable create(RemoteFileRegistered registered) {
		FileId fileId = registered.getFileId();
		File file = new File(getDefaultPath() + registered.getName());
		MemorySize size = registered.getSize();
		
		GUIFileWriteableTreeTableRow newFile = new GUIFileWriteableTreeTableRow(
															fileId, file, size);
		
		newFile.setPriority(getDefaultPriority());
		return newFile;
	}
}
