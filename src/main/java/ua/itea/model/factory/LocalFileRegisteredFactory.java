package ua.itea.model.factory;

import java.io.File;

import ua.itea.model.LocalFileRegistered;
import ua.itea.model.MemorySize;

public class LocalFileRegisteredFactory {
	public LocalFileRegistered create(File file) {
		return new LocalFileRegistered(file, new MemorySize(file.length()));
	}
}
