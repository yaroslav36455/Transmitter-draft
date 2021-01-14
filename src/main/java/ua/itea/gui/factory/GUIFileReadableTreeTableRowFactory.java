package ua.itea.gui.factory;

import java.io.File;

import ua.itea.gui.GUIFileReadableTreeTableRow;
import ua.itea.model.FileId;
import ua.itea.model.LocalFileReadable;
import ua.itea.model.factory.LocalFileReadableFactory;

public class GUIFileReadableTreeTableRowFactory extends LocalFileReadableFactory {
	@Override
	public LocalFileReadable create(File file) {
		return new GUIFileReadableTreeTableRow(new FileId(), file);
	}
}
