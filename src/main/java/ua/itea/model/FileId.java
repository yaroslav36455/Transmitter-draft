package ua.itea.model;

import java.io.Serializable;

public class FileId implements Serializable {
	private static final long serialVersionUID = -8594382399121010455L;
	private static int ID = 0;
	private final int id;

	public FileId() {
		this.id = ID++;
	}

	public int get() {
		return id;
	}
}
