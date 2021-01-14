package ua.itea.model.factory;

import java.io.File;

import ua.itea.model.FileId;
import ua.itea.model.LocalFileReadable;

public class LocalFileReadableFactory {
	public LocalFileReadable create(File file) {
		return new LocalFileReadable(new FileId(), file);
	}
}
